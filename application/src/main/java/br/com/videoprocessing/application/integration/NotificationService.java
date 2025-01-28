package br.com.videoprocessing.application.integration;

import br.com.videoprocessing.application.infra.RabbitMQConfig;
import br.com.videoprocessing.application.service.CompactVideoToZipService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final CompactVideoToZipService compactVideoToZipService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String videoProcessingId) {
        compactVideoToZipService.procesVideo(videoProcessingId);
    }
}
