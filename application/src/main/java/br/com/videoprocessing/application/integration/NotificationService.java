package br.com.videoprocessing.application.integration;

import br.com.videoprocessing.application.infra.RabbitMQConfig;
import br.com.videoprocessing.application.service.VideoProcessingApplicationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final VideoProcessingApplicationService videoProcessingApplicationService;

    public NotificationService(VideoProcessingApplicationService videoProcessingApplicationService) {
        this.videoProcessingApplicationService = videoProcessingApplicationService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String videoProcessingId) {
        videoProcessingApplicationService.proccesVideo(videoProcessingId);
    }
}
