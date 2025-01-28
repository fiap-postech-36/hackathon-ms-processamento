package br.com.videoprocessing.infra.repository;

import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.messages.Bucket;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public InputStream downloadVideoCopy(String urlDoVideo) {
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(urlDoVideo)
                .build())) {
            return copiarVideoParaByteArrayParaNaoFecharConexao(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar o vídeo", e);
        }
    }

    public void uploadZip(byte[] zipData, String objectName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(zipData), zipData.length, -1)
                            .contentType("application/zip")
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o zip", e);
        }
    }

    private ByteArrayInputStream copiarVideoParaByteArrayParaNaoFecharConexao(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        inputStream.transferTo(buffer);
        return new ByteArrayInputStream(buffer.toByteArray());
    }
}
