package br.com.videoprocessing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVideoProcessingDTO {

    private String usuarioId;
    private String emailDoUsuario;
    private String urlDoVideo;
}
