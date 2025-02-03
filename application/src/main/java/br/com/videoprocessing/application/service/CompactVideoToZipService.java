package br.com.videoprocessing.application.service;

import br.com.videoprocessing.application.dto.EmailRabbitDTO;
import br.com.videoprocessing.application.infra.RabbitMQConfig;
import br.com.videoprocessing.domain.core.domain.entities.VideoProcessing;
import br.com.videoprocessing.infra.repository.MinioRepository;
import br.com.videoprocessing.infra.repository.VideoProcessingRepository;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class CompactVideoToZipService {

    private static final Integer TIME_INTERVAL_IN_SECONDS = 20;

    private final VideoProcessingRepository videoProcessingRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MinioRepository minioRepository;

    public void procesVideo(String videoProcessingId) {
        VideoProcessing videoProcessing = videoProcessingRepository.findById(videoProcessingId).orElseThrow(() -> new RuntimeException("Invalid id to procces video."));
        try {
            changeStatusToProcessing(videoProcessing);
            InputStream videoStream = minioRepository.downloadVideoCopy(videoProcessing.getUrlDoVideo());
            ByteArrayOutputStream zipOfTheImagesOnMemory = createZipOfTheImagesOnMemory(videoStream);
            minioRepository.uploadZip(zipOfTheImagesOnMemory.toByteArray(), videoProcessing.getId() + ".zip");
            rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE_NAME, RabbitMQConfig.EMAIL_KEY_NAME,
                    new EmailRabbitDTO(videoProcessing.getEmailDoUsuario(), "SUCCESS", "Deu bom!!!"));
            changeStatusToFinished(videoProcessing);
        } catch (Exception e) {
            changeStatusToFailed(videoProcessing);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE_NAME, RabbitMQConfig.EMAIL_KEY_NAME,
                    new EmailRabbitDTO(videoProcessing.getEmailDoUsuario(), "ERROR", "Deu ruim!!!"));
            throw new RuntimeException(e);
        }
    }

    private ByteArrayOutputStream createZipOfTheImagesOnMemory(InputStream videoStream) throws FFmpegFrameGrabber.Exception {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoStream);
        frameGrabber.start();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            Frame frame;
            int frameNumber = 0;
            int framesToSkip = (int) frameGrabber.getFrameRate() * TIME_INTERVAL_IN_SECONDS;
            Java2DFrameConverter converter = new Java2DFrameConverter();
            while ((frame = frameGrabber.grabFrame()) != null) {
                if (frameNumber % framesToSkip == 0 && frame.image != null) {
                    BufferedImage image = converter.getBufferedImage(frame);
                    saveFrameAsImage(image, frameNumber / framesToSkip, zipOut);
                }
                frameNumber++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            frameGrabber.stop();
        }
        return byteArrayOutputStream;
    }

    private void saveFrameAsImage(BufferedImage image, int frameIndex, ZipOutputStream zipOut) throws IOException {
        String frameName = String.format("frame-%03d.jpg", frameIndex);
        ZipEntry zipEntry = new ZipEntry(frameName);
        zipOut.putNextEntry(zipEntry);
        ImageIO.write(image, "jpg", zipOut);
        zipOut.closeEntry();
    }

    private void changeStatusToFailed(VideoProcessing videoProcessing) {
        videoProcessing.setProcessingStatusToFailed();
        videoProcessingRepository.save(videoProcessing);
    }

    private void changeStatusToFinished(VideoProcessing videoProcessing) {
        videoProcessing.setProcessingStatusToFinished();
        videoProcessingRepository.save(videoProcessing);
    }

    private void changeStatusToProcessing(VideoProcessing videoProcessing) {
        videoProcessing.setProcessingStatusToProcessing();
        videoProcessingRepository.save(videoProcessing);
    }
}
