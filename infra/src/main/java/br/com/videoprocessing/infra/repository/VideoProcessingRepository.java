package br.com.videoprocessing.infra.repository;

import br.com.videoprocessing.domain.core.domain.entities.VideoProcessing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VideoProcessingRepository extends MongoRepository<VideoProcessing, String> {

    public List<VideoProcessing> findAllByUsuarioId(String usuarioId);
}
