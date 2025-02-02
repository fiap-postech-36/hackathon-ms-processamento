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
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompactVideoToZipServiceTest {

    @Mock
    private VideoProcessingRepository videoProcessingRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private MinioRepository minioRepository;

    @InjectMocks
    private CompactVideoToZipService compactVideoToZipService;

    private VideoProcessing videoProcessing;

    @BeforeEach
    void setUp() {
        videoProcessing = new VideoProcessing("123", "test@example.com", "video-url.mp4");
    }

//    @Test
//    void shouldProcessVideoSuccessfully() throws Exception {
//        when(videoProcessingRepository.findById("123")).thenReturn(Optional.of(videoProcessing));
//        when(minioRepository.downloadVideoCopy(anyString())).thenReturn(new ByteArrayInputStream(new byte[0]));
//
//        compactVideoToZipService.procesVideo("123");
//
//        verify(videoProcessingRepository, times(2)).save(any(VideoProcessing.class));
//        verify(minioRepository, times(1)).uploadZip(any(byte[].class), anyString());
//        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.EMAIL_EXCHANGE_NAME), eq(RabbitMQConfig.EMAIL_KEY_NAME), any(EmailRabbitDTO.class));
//    }

    @Test
    void shouldHandleInvalidVideoProcessingId() {
        when(videoProcessingRepository.findById("invalid-id")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> compactVideoToZipService.procesVideo("invalid-id"));
        assertEquals("Invalid id to procces video.", exception.getMessage());
    }

//    @Test
//    void shouldHandleProcessingFailure() throws Exception {
//        when(videoProcessingRepository.findById("123")).thenReturn(Optional.of(videoProcessing));
//        when(minioRepository.downloadVideoCopy(anyString())).thenThrow(new RuntimeException("Download error"));
//
//        assertThrows(RuntimeException.class, () -> compactVideoToZipService.procesVideo("123"));
//
//        InOrder inOrder = inOrder(videoProcessingRepository);
//        inOrder.verify(videoProcessingRepository).save(argThat(v -> v.getProcessingStatus().equals("PROCESSING")));
//        inOrder.verify(videoProcessingRepository).save(argThat(v -> v.getProcessingStatus().equals("FAILED")));
//
//        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.EMAIL_EXCHANGE_NAME), eq(RabbitMQConfig.EMAIL_KEY_NAME), any(EmailRabbitDTO.class));
//    }


}
