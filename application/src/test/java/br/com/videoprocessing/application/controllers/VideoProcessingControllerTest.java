package br.com.videoprocessing.application.controllers;

import br.com.videoprocessing.application.service.VideoProcessingApplicationService;
import br.com.videoprocessing.application.service.VideoProcessingDTO;
import br.com.videoprocessing.domain.core.domain.entities.ProcessingStatus;
import br.com.videoprocessing.domain.core.domain.entities.VideoProcessing;
import br.com.videoprocessing.infra.repository.MinioRepository;
import br.com.videoprocessing.infra.repository.VideoProcessingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class VideoProcessingControllerTest {

    @MockBean
    private VideoProcessingRepository videoProcessingRepository;

    @MockBean
    private VideoProcessingApplicationService videoProcessingApplicationService;

    @Autowired
    private VideoProcessingController videoProcessingController;

    @MockBean
    private MinioRepository minioRepository; // Mock do MinioRepository

    @MockBean
    private JwtDecoder jwtDecoder; // Mock do JwtDecoder


    @Test
    void testGetVideosProcessing() {

        VideoProcessing videoProcessing = new VideoProcessing("usuarioId", "email@example.com", "http://example.com/video.mp4");
        List<VideoProcessing> videoProcessingList = List.of(videoProcessing);
        when(videoProcessingRepository.findAll()).thenReturn(videoProcessingList);


        ResponseEntity<List<VideoProcessing>> response = videoProcessingController.getVideosProcessing();


        assertEquals(200, response.getStatusCodeValue());
        assertEquals(videoProcessingList, response.getBody());
        verify(videoProcessingRepository, times(1)).findAll();
    }

    @Test
    void testGetVideosProcessingByUsuario() {

        String usuarioId = "usuarioId";
        VideoProcessingDTO videoProcessingDTO = new VideoProcessingDTO("http://example.com/video.mp4", "http://example.com/zip.zip", ProcessingStatus.FINISHED);
        List<VideoProcessingDTO> videoProcessingDTOList = List.of(videoProcessingDTO);
        when(videoProcessingApplicationService.buscarTodosPorIdDoUsuario(usuarioId)).thenReturn(videoProcessingDTOList);


        ResponseEntity<List<VideoProcessingDTO>> response = videoProcessingController.getVideosProcessingByUsuario(usuarioId);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals(videoProcessingDTOList, response.getBody());
        verify(videoProcessingApplicationService, times(1)).buscarTodosPorIdDoUsuario(usuarioId);
    }
}