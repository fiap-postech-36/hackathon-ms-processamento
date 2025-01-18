package br.com.videoprocessing.application.service;

import br.com.videoprocessing.application.infra.RabbitMQConfig;
import br.com.videoprocessing.domain.core.domain.entities.VideoProcessing;
import br.com.videoprocessing.infra.repository.MinioRepository;
import br.com.videoprocessing.infra.repository.VideoProcessingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoProcessingApplicationService {

    private final VideoProcessingRepository videoProcessingRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MinioRepository minioRepository;

    public String createVideoProcessing(CreateVideoProcessingDTO createVideoProcessingDTO) {
        VideoProcessing videoProcessing = videoProcessingRepository.save(new VideoProcessing(createVideoProcessingDTO.getUsuarioId(), createVideoProcessingDTO.getUrlDoVideo()));
        String videoProcessingId = videoProcessing.getId();
        sendVideoProcessingToQueue(videoProcessingId);
        return videoProcessingId;
    }

    private void sendVideoProcessingToQueue(String id) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.KEY_NAME, id);
        System.out.println("Mensagem enviada: " + id);
    }

    public List<VideoProcessingDTO> buscarTodosPorIdDoUsuario(String usuarioId) {
        List<VideoProcessing> videosProcessing = videoProcessingRepository.findAllByUsuarioId(usuarioId);
        return videosProcessing.stream().map(videoProcessing -> new VideoProcessingDTO(videoProcessing.getUrlDoVideo(), videoProcessing.getUrlDoZip(), videoProcessing.getProcessingStatus())).toList();
    }

    //talvez isolar em um serviço a parte
    public String proccesVideo(String videoProcessingId) {
        try {
            VideoProcessing videoProcessing = videoProcessingRepository.findById(videoProcessingId).orElseThrow(() -> new RuntimeException("Invalid id to procces video."));
            InputStream inputStream = minioRepository.downloadVideo(videoProcessing.getUrlDoVideo());
            //processar o video
            //zipar imagens
            //enviar zip para o minIO
            rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE_NAME, RabbitMQConfig.EMAIL_KEY_NAME,
                    new EmailRabbitDTO("ulysses903@gmail.com", "SUCCESS", "Deu bom!!!"));
            return videoProcessingId;
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE_NAME, RabbitMQConfig.EMAIL_KEY_NAME,
                    new EmailRabbitDTO("ulysses903@gmail.com", "ERROR", "Deu ruim!!!"));
            throw new RuntimeException(e);
        }
    }
}
