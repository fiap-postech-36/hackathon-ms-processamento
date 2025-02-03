package br.com.videoprocessing.application.integration;

import br.com.videoprocessing.application.service.CompactVideoToZipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private CompactVideoToZipService compactVideoToZipService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldReceiveMessageAndProcessVideo() {
        String videoProcessingId = "123";
        notificationService.receiveMessage(videoProcessingId);
        verify(compactVideoToZipService).procesVideo(videoProcessingId);
    }

    @Test
    void shouldNotProcessVideoWhenIdIsNull() {
        String videoProcessingId = null;
        notificationService.receiveMessage(videoProcessingId);
        verify(compactVideoToZipService, never()).procesVideo(anyString());
    }

}
