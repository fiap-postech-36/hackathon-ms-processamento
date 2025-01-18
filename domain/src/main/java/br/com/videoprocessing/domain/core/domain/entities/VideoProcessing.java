package br.com.videoprocessing.domain.core.domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "videosProcessing")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VideoProcessing implements Serializable {

    @Id
    private String id;
    private String usuarioId;
    private String urlDoVideo;
    private String urlDoZip;
    private ProcessingStatus processingStatus;
    private LocalDateTime processingStatusUpdateDate;

    public VideoProcessing(String usuarioId, String urlDoVideo) {
        this.usuarioId = usuarioId;
        this.urlDoVideo = urlDoVideo;
        this.processingStatusUpdateDate = LocalDateTime.now();
        this.processingStatus = ProcessingStatus.PENDING;
    }

    public void setProcessingStatusToFailed() {
        this.processingStatusUpdateDate = LocalDateTime.now();
        this.processingStatus = ProcessingStatus.PROCESSING;
    }
}
