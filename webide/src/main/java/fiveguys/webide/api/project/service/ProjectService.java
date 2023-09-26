package fiveguys.webide.api.project.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import fiveguys.webide.api.project.dto.request.*;
import fiveguys.webide.api.project.dto.response.*;
import fiveguys.webide.common.error.ErrorCode;
import fiveguys.webide.common.error.GlobalException;
import fiveguys.webide.config.auth.LoginUser;
import fiveguys.webide.domain.invite.Invite;
import fiveguys.webide.domain.project.Project;
import fiveguys.webide.repository.invite.InviteRepository;
import fiveguys.webide.repository.project.ProjectRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final AmazonS3Client amazonS3Client;
    private final ProjectRepository projectRepository;
    private final InviteRepository inviteRepository;
    private String localLocation = "/src/main/resources/tempStore/";

    @PostConstruct
    public void init() {
        localLocation = System.getProperty("user.dir") + localLocation;
    }

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
      
    @Transactional
    public CreateRepoResponse createRepo(LoginUser loginUser, String repoName, MultipartFile file) throws IOException {
        String nickname = loginUser.getUser().getNickname();

        String uploadFileName = file.getOriginalFilename();
        String projectName = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
        System.out.println("projectName = " + projectName);

        String uploadFullPath = localLocation + uploadFileName;
        File localUploadFile = new File(uploadFullPath);
        file.transferTo(localUploadFile);

        ZipFile zipFile = new ZipFile(uploadFullPath);
        String unzipPath = localLocation;
        zipFile.setCharset(Charset.forName("UTF-8"));
        zipFile.extractAll(unzipPath);

        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        for (FileHeader fileHeader : fileHeaders) {
            System.out.println(fileHeader.getFileName());
            if (fileHeader.isDirectory()) {
                ObjectMetadata metadata = new ObjectMetadata();
                byte[] content = new byte[0];
                metadata.setContentLength(0);

                amazonS3Client.putObject(bucket,nickname + "/" + fileHeader.getFileName(),new ByteArrayInputStream(content),metadata);
            } else {
                File localUnzipFile = new File(unzipPath + fileHeader.getFileName());

                amazonS3Client.putObject(new PutObjectRequest(bucket, nickname + "/" + fileHeader.getFileName(), localUnzipFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }
        }

        deleteDirectory(new File(localLocation + projectName));
        localUploadFile.delete();

        Project saveProject = projectRepository.save(Project.builder()
                .userId(loginUser.getUser().getId())
                .repoName(repoName)
                .projectName(projectName)
                .bookmark(false)
                .build());

        return new CreateRepoResponse(saveProject.getId(), projectName);
    }
    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    boolean success = deleteDirectory(child);
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        return dir.delete();
    }

    public FileTreeResponse fileTree(String nickname, String projectName) {
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, nickname + "/" + projectName);
        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        FileTreeResponse data = new FileTreeResponse(projectName, "folder");
        for(S3ObjectSummary s3object : s3ObjectSummaries){
            String fileKey = s3object.getKey();
            String fileName = fileKey.replace(nickname + "/" + projectName + "/", "");

            String[] fileParts = fileName.split("/");
            data.insert(fileParts, 0);
        }

        return data;
    }


    @Transactional
    public void deleteRepo(String nickname, Long repoId) {
        Project findProject = projectRepository.findById(repoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_EXIST_PROJECT));

        String projectName = findProject.getProjectName();
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, nickname + "/" + projectName);
        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        for(S3ObjectSummary s3object : s3ObjectSummaries){
            String fileKey = s3object.getKey();
            amazonS3Client.deleteObject(bucket, fileKey);
        }

        projectRepository.delete(findProject);

        List<Invite> findInvites = inviteRepository.findAllByProjectId(repoId);
        inviteRepository.deleteAllInBatch(findInvites);
    }

    @Transactional
    public void changeRepoName(Long repoId, String repoName) {
        Project findProject = projectRepository.findById(repoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_EXIST_PROJECT));

        findProject.changeRepoName(repoName);
    }

    public MyRepoListResponse myRepoList(Long userId) {
        List<Project> findProjectList = projectRepository.findAllByUserId(userId);
        long repoCnt = findProjectList.stream().count();

        MyRepoListResponse data = new MyRepoListResponse();
        for (Project findProject : findProjectList) {
            List<InvitedUser> findIvitedUserList = inviteRepository.findInviteListByProjectId(findProject.getId());

            data.getRepoList().add(RepoInfo.builder()
                    .repoId(findProject.getId())
                    .repoName(findProject.getRepoName())
                    .createdAt(findProject.getCreatedAt())
                    .updatedAt(findProject.getModifiedAt())
                    .bookmark(findProject.isBookmark())
                    .invitedUser(findIvitedUserList)
                    .invitedUserCnt(findIvitedUserList.stream().count())
                    .build());
        }
        data.setRepoCnt(repoCnt);

        return data;
    }

    public InvitedRepoListResponse invitedRepoList(Long userId) {
        InvitedRepoListResponse data = new InvitedRepoListResponse();

        List<InvitedRepoInfo> projectListByUserId = inviteRepository.findProjectListByUserId(userId);
        data.setRepoList(projectListByUserId);

        data.setRepoCnt(projectListByUserId.stream().count());

        return data;
    }


    @Transactional
    public void bookmarkRepo(Long repoId) {
        Project findProject = projectRepository.findById(repoId).get();
        findProject.changeBookmark();
    }
}

