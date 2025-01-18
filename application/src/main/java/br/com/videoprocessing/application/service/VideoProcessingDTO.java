package br.com.videoprocessing.application.service;

import br.com.videoprocessing.domain.core.domain.entities.ProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoProcessingDTO {

    private String urlDoVideo;
    private String urlDoZip;
    private ProcessingStatus processingStatus;
}
