package fiveguys.webide.api.project.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import fiveguys.webide.api.project.dto.request.FileCreateRequest;
import fiveguys.webide.api.project.dto.request.FileRenameRequest;
import fiveguys.webide.api.project.dto.request.FolderCreateRequest;
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

}
