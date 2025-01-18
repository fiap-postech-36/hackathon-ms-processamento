package br.com.videoprocessing.infra.repository;

import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class MinioRepository {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioRepository(MinioClient minioClient) {
        this.minioClient = minioClient;
        this.bucketName = "videos-hackaton";
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                System.out.println("Bucket criado: " + bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar o bucket", e);
        }
    }

    private boolean bucketExists(String bucketName) throws Exception {
        List<Bucket> buckets = minioClient.listBuckets();
        return buckets.stream().anyMatch(b -> b.name().equals(bucketName));
    }

    public InputStream downloadVideo(String urlDoVideo) {
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(urlDoVideo)
                .build())) {
            return inputStream;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar o vídeo", e);
        }
    }
}
