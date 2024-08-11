package com.mohit.docservice.Services;

import com.mohit.docservice.Utils.Exceptions.FileExistsException;
import com.mohit.docservice.Utils.Exceptions.NoSuchFileException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Multipart;

import java.util.Optional;

@Service
public class MinioService {
    private final MinioClient minioClient;
    @Value("${spring.data.minio.bucket}")
    private String bucket;
    MinioService(MinioClient minioClient){
        this.minioClient = minioClient;
    }

//    private boolean makeBucket(String userId){
//        try{
//            minioClient.makeBucket(MakeBucketArgs.builder().bucket(userId).build());
//            return true;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private boolean isBucketPresent(String userId){
//        try{
//            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(userId).build());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }

    private boolean fileExists(String filePath){
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(filePath).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean uploadFile(String userId, MultipartFile file, String filePath) throws FileExistsException{
        if(fileExists(filePath))
            throw new FileExistsException("File with path: " + filePath + " exists already!!!");
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(filePath)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            minioClient.putObject(args);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Optional<InputStreamResource> downloadFile(String filePath) throws NoSuchFileException{
        if(!fileExists(filePath))
            throw new NoSuchFileException("No such file at path: " + filePath);
        try{
            return Optional.of(new InputStreamResource(
                    minioClient.getObject(GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(filePath)
                            .build())
                    ));
        }
        catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
