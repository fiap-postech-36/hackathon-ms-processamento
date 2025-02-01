package br.com.videoprocessing.application.service;

import br.com.videoprocessing.application.dto.CreateVideoProcessingDTO;
import br.com.videoprocessing.application.dto.VideoProcessingDTO;
import br.com.videoprocessing.domain.core.domain.entities.VideoProcessing;
import br.com.videoprocessing.infra.repository.VideoProcessingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VideoProcessingApplicationServiceTest {

    @Mock
    private VideoProcessingRepository videoProcessingRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private VideoProcessingApplicationService videoProcessingApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createVideoProcessing_ShouldReturnVideoProcessingId2() {
        CreateVideoProcessingDTO dto = new CreateVideoProcessingDTO("ibraim", "leandroibraimads@gmail.com", "http://example.com/video.mp4");
        VideoProcessing videoProcessing = new VideoProcessing(dto.getUsuarioId(), dto.getEmailDoUsuario(), dto.getUrlDoVideo());
        when(videoProcessingRepository.save(any(VideoProcessing.class))).thenReturn(videoProcessing);

        String result = videoProcessingApplicationService.createVideoProcessing(dto);

        assertEquals(videoProcessing.getId(), result);
        verify(videoProcessingRepository, times(1)).save(any(VideoProcessing.class));

        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(rabbitTemplate, times(1)).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());

        assertEquals("video-processing-exchange", exchangeCaptor.getValue());
        assertEquals("video.processing.routing.key", routingKeyCaptor.getValue());
        Assertions.assertNull(messageCaptor.getValue());
    }

    @Test
    void buscarTodosPorIdDoUsuario_ShouldReturnListOfVideoProcessingDTO() {

        String usuarioId = "usuarioId";
        VideoProcessing videoProcessing = new VideoProcessing(usuarioId, "ibraim", "http://example.com/video.mp4");
        when(videoProcessingRepository.findAllByUsuarioId(usuarioId)).thenReturn(List.of(videoProcessing));

        List<VideoProcessingDTO> result = videoProcessingApplicationService.buscarTodosPorIdDoUsuario(usuarioId);

        assertEquals(1, result.size());
        assertEquals(videoProcessing.getUrlDoVideo(), result.get(0).getUrlDoVideo());
        assertEquals(videoProcessing.getUrlDoZip(), result.get(0).getUrlDoZip());
        assertEquals(videoProcessing.getProcessingStatus(), result.get(0).getProcessingStatus());
        verify(videoProcessingRepository, times(1)).findAllByUsuarioId(usuarioId);
    }
}