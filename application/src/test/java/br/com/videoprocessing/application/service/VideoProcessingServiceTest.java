package br.com.videoprocessing.application.service;

import br.com.videoprocessing.application.dto.CreateVideoProcessingDTO;
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

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoProcessingServiceTest {

    @Mock
    private VideoProcessingRepository videoProcessingRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private MinioRepository minioRepository;

    @InjectMocks
    private CompactVideoToZipService compactVideoToZipService;

    @InjectMocks
    private VideoProcessingApplicationService videoProcessingApplicationService;

    private VideoProcessing videoProcessing;

    @BeforeEach
    void setUp() {
        videoProcessing = new VideoProcessing("ibraim", "leandroibraimads@gmail.com", "video-url");
    }

    @Test
    void shouldHandleInvalidVideoProcessingId() {
        when(videoProcessingRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> compactVideoToZipService.procesVideo("invalid-id"));
        assertEquals("Invalid id to procces video.", exception.getMessage());
    }

    @Test
    void shouldCreateVideoProcessingSuccessfully() {
        // Criando um objeto válido de VideoProcessing
        VideoProcessing savedVideoProcessing = new VideoProcessing("ibraim", "leandroibraimads@gmail.com", "video-url");
        savedVideoProcessing.setId("generated-id");  // Simulando um ID válido

        // Configurando o mock para retornar um objeto válido
        when(videoProcessingRepository.save(any())).thenReturn(savedVideoProcessing);

        // Chamando o método de teste
        String videoProcessingId = videoProcessingApplicationService.createVideoProcessing(
                new CreateVideoProcessingDTO("ibraim", "leandroibraimads@gmail.com", "video-url"));

        // Validações
        assertNotNull(videoProcessingId, "O ID do processamento de vídeo não deveria ser nulo.");
        assertEquals("generated-id", videoProcessingId, "O ID retornado deveria ser 'generated-id'.");

        // Verificações dos mocks
        verify(videoProcessingRepository, times(1)).save(any());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());
    }


}
