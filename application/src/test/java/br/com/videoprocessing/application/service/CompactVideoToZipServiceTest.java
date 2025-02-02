package br.com.videoprocessing.application.service;

import br.com.videoprocessing.application.dto.EmailRabbitDTO;
import br.com.videoprocessing.application.infra.RabbitMQConfig;
import br.com.videoprocessing.domain.core.domain.entities.VideoProcessing;
import br.com.videoprocessing.infra.repository.MinioRepository;
import br.com.videoprocessing.infra.repository.VideoProcessingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompactVideoToZipServiceTest {

    // Mocks para os repositórios e o template de e-mail
    @Mock
    private VideoProcessingRepository videoProcessingRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private MinioRepository minioRepository;

    // Serviço de compactação de vídeo em ZIP
    @InjectMocks
    private CompactVideoToZipService compactVideoToZipService;

    // Objeto de vídeo para teste
    private VideoProcessing videoProcessing;


    @BeforeEach
    void setUp() {
        videoProcessing = new VideoProcessing("123", "test@example.com", "video-url.mp4");
    }

    /**
     * Testa se o serviço de compactação de vídeo em ZIP funciona corretamente.
     */
    @Test
    void shouldProcessVideoSuccessfully() {
        // Mocka o repositório de vídeo para retornar o objeto de vídeo
        when(videoProcessingRepository.findById("123")).thenReturn(Optional.of(videoProcessing));

        // Carrega um arquivo de vídeo fake do dir resources
        String fakeVideoStreamPath = "/video-de-teste.mp4";
        InputStream fakeVideoStream = getClass().getResourceAsStream(fakeVideoStreamPath);

        // Mocka o repositório de Minio para retornar o arquivo de vídeo fake
        when(minioRepository.downloadVideoCopy(anyString())).thenReturn(fakeVideoStream);

        // Chama o serviço de compactação de vídeo em ZIP passando ID 123
        compactVideoToZipService.procesVideo("123");

        // Verifica as interações com os repositórios e o template de e-mail
        verify(videoProcessingRepository, times(2)).save(any());
        verify(minioRepository).uploadZip(any(), anyString());
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.EMAIL_EXCHANGE_NAME), eq(RabbitMQConfig.EMAIL_KEY_NAME), any(EmailRabbitDTO.class));
    }

    /**
     * Testa se o serviço de compactação de vídeo em ZIP lida corretamente com falhas de processamento.
     */
    @Test
    void shouldHandleProcessingFailure() {
        // Mocka o repositório de vídeo para retornar o objeto de vídeo
        when(videoProcessingRepository.findById("123")).thenReturn(Optional.of(videoProcessing));

        // Mocka o repositório de Minio para lançar uma exceção de download
        when(minioRepository.downloadVideoCopy(anyString())).thenThrow(new RuntimeException("Download error"));

        // Verifica se o serviço de compactação de vídeo em ZIP lança uma exceção de processamento
        assertThrows(RuntimeException.class, () -> {
            try {
                compactVideoToZipService.procesVideo("123");
            } catch (Exception e) {
                // Verifica se o template de e-mail foi enviado
                verify(rabbitTemplate).convertAndSend(
                        eq(RabbitMQConfig.EMAIL_EXCHANGE_NAME),
                        eq(RabbitMQConfig.EMAIL_KEY_NAME),
                        any(EmailRabbitDTO.class)
                );
                throw e;
            }
        });

        // Verifica se o repositório de vídeo foi salvo corretamente
        verify(videoProcessingRepository, times(2)).save(any(VideoProcessing.class));
    }

    /**
     * Testa se o serviço de compactação de vídeo em ZIP lida corretamente com IDs de vídeo inválidos.
     */
    @Test
    void shouldHandleInvalidVideoProcessingId() {
        // Mocka o repositório de vídeo para retornar um objeto vazio
        when(videoProcessingRepository.findById("invalid-id")).thenReturn(Optional.empty());

        // Verifica se o serviço de compactação de vídeo em ZIP lança uma exceção de ID inválido
        Exception exception = assertThrows(RuntimeException.class, () -> compactVideoToZipService.procesVideo("invalid-id"));
        assertEquals("Invalid id to procces video.", exception.getMessage());
    }
}