package fiveguys.webide.api.project.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Builder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import fiveguys.webide.api.project.dto.request.*;
import fiveguys.webide.api.project.dto.response.FileReadResponse;
import fiveguys.webide.common.dto.ResponseDto;
import fiveguys.webide.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final AmazonS3Client amazonS3Client;
    private final ProjectRepository projectRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void fileCreate(FileCreateRequest fileCreateRequest) {
        try{
            String fileName = fileCreateRequest.getPath()+"/"+fileCreateRequest.getFileName();

            ObjectMetadata metadata = new ObjectMetadata();
            byte[] content = new byte[0];
            metadata.setContentLength(0);
            amazonS3Client.putObject(bucket,fileName,new ByteArrayInputStream(content),metadata);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void folderCreate(FolderCreateRequest folderCreateRequest){
        try{
            String fileName = folderCreateRequest.getPath()+"/"+folderCreateRequest.getFolderName()+"/";

            ObjectMetadata metadata = new ObjectMetadata();
            byte[] content = new byte[0];
            metadata.setContentLength(0);
            amazonS3Client.putObject(bucket,fileName,new ByteArrayInputStream(content),metadata);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public FileReadResponse fileRead(String path) {
        try {
            S3Object s3Object = amazonS3Client.getObject(bucket, path);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(objectInputStream));
            StringBuilder content = new StringBuilder();
            String line;
            int lines = 1;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
                lines += 1;
            }
            reader.close();
            return FileReadResponse.builder()
                    .body(content.toString())
                    .lines(lines)
                    .size(248)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return FileReadResponse.builder().build();
        }
    }

    public void fileFolderDelete(String path) {
        try {
            amazonS3Client.deleteObject(bucket, path);
        } catch (AmazonServiceException e) {
            // AmazonS3 서비스 예외 처리
            e.printStackTrace();
        } catch (AmazonClientException e) {
            // AmazonS3 클라이언트 예외 처리
            e.printStackTrace();
        }
    }
    public void fileRename(FileRenameRequest fileRenameRequest, String path){
        try {
            // 기존 파일의 내용을 읽어옵니다.
            S3Object s3Object = amazonS3Client.getObject(bucket, path);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(objectInputStream));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            // 기존 파일을 삭제합니다.
            amazonS3Client.deleteObject(bucket, path);

            // 새로운 경로에 파일을 복사하여 저장합니다.
            String newFileName = fileRenameRequest.getPath()+"/"+fileRenameRequest.getFileName();
            ObjectMetadata metadata = new ObjectMetadata();
            byte[] contentBytes = content.toString().getBytes(); // 파일 내용을 바이트 배열로 변환
            metadata.setContentLength(contentBytes.length);
            amazonS3Client.putObject(bucket, newFileName, new ByteArrayInputStream(contentBytes), metadata);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void folderRename(FolderRenameRequest folderRenameRequest, String path){
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, path);
        for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()){
            String sourceObjectKey = objectSummary.getKey();
            String destinationKey = sourceObjectKey.replace(path, folderRenameRequest.getPath()+"/"+folderRenameRequest.getFolderName());
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, sourceObjectKey, bucket, destinationKey);
            amazonS3Client.copyObject(copyObjectRequest);
        }
        ObjectListing sourceFolderObjects = amazonS3Client.listObjects(bucket, path);
        for (S3ObjectSummary objectSummary : sourceFolderObjects.getObjectSummaries()) {
            String objectKey = objectSummary.getKey();
            amazonS3Client.deleteObject(bucket, objectKey);
        }
    }

    public void fileChangeBody(FileNewBodyRequest fileNewBodyRequest,String path) {
        byte[] newContentBytes = fileNewBodyRequest.getBody().getBytes();
        amazonS3Client.deleteObject(bucket, path);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(newContentBytes.length);
        amazonS3Client.putObject(bucket, path, new ByteArrayInputStream(newContentBytes), metadata);
    }
}

