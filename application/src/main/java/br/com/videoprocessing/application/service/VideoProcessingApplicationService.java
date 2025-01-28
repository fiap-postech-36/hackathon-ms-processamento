package br.com.videoprocessing.application.service;

import br.com.videoprocessing.application.infra.RabbitMQConfig;
import br.com.videoprocessing.domain.core.domain.entities.VideoProcessing;
import br.com.videoprocessing.infra.repository.VideoProcessingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoProcessingApplicationService {

    private final VideoProcessingRepository videoProcessingRepository;
    private final RabbitTemplate rabbitTemplate;

    public String createVideoProcessing(CreateVideoProcessingDTO createVideoProcessingDTO) {
        VideoProcessing videoProcessing = videoProcessingRepository.save(new VideoProcessing(createVideoProcessingDTO.getUsuarioId(), createVideoProcessingDTO.getEmailDoUsuario(), createVideoProcessingDTO.getUrlDoVideo()));
        String videoProcessingId = videoProcessing.getId();
        sendVideoProcessingToQueue(videoProcessingId);
        return videoProcessingId;
    }

    private void sendVideoProcessingToQueue(String id) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.KEY_NAME, id);
    }

    public List<VideoProcessingDTO> buscarTodosPorIdDoUsuario(String usuarioId) {
        List<VideoProcessing> videosProcessing = videoProcessingRepository.findAllByUsuarioId(usuarioId);
        return videosProcessing.stream().map(videoProcessing -> new VideoProcessingDTO(videoProcessing.getUrlDoVideo(), videoProcessing.getUrlDoZip(), videoProcessing.getProcessingStatus())).toList();
    }
}
