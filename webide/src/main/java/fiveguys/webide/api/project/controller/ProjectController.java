package fiveguys.webide.api.project.controller;

import fiveguys.webide.api.project.dto.request.ChangeRepoNameRequest;
import fiveguys.webide.api.project.dto.request.FileCreateRequest;
import fiveguys.webide.api.project.dto.request.FolderCreateRequest;
import fiveguys.webide.api.project.dto.response.FileReadResponse;
import fiveguys.webide.api.project.dto.response.FileTreeResponse;
import fiveguys.webide.api.project.dto.response.InvitedRepoList;
import fiveguys.webide.api.project.dto.response.MyRepoListResponse;
import fiveguys.webide.api.project.service.ProjectService;
import fiveguys.webide.common.dto.ResponseDto;
import fiveguys.webide.config.auth.LoginUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repo")
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping("/file")
    public ResponseDto<Void> fileCreate(@RequestBody FileCreateRequest fileCreateRequest){
        projectService.fileCreate(fileCreateRequest);
        return ResponseDto.success("파일 생성 성공",null);
    }
    @PostMapping("/folder")
    public ResponseDto<Void> folderCreate(@RequestBody FolderCreateRequest folderCreateRequest){
        projectService.folderCreate(folderCreateRequest);
        return ResponseDto.success("폴더 생성 성공",null);
    }
    @GetMapping("/file/tree/{*path}")
    public ResponseDto<FileReadResponse> fileRead(@PathVariable String path){
        return ResponseDto.success("파일 보기 성공",projectService.fileRead(path));
    }
    @DeleteMapping("/file/tree/{*path}")
    public ResponseDto<Void> fileDelete(@PathVariable String path){
        projectService.fileFolderDelete(path);
        return ResponseDto.success("파일 삭제 성공",null);
    }
    @DeleteMapping("/folder/tree/{*path}")
    public ResponseDto<Void> folderDelete(@PathVariable String path){
        projectService.fileFolderDelete(path);
        return ResponseDto.success("폴더 삭제 성공", null);
    }

    @PostMapping
    public ResponseDto<Void> createRepo(@AuthenticationPrincipal LoginUser loginUser,
                                        @RequestPart("repoName") String repoName,
                                        @RequestPart("file") MultipartFile file) throws IOException {
        projectService.createRepo(loginUser, repoName, file);

        return ResponseDto.success("레포 생성 성공", null);
    }

    @GetMapping("/{nickname}/{projectName}")
    public ResponseDto<FileTreeResponse> fileTree(@PathVariable String nickname,
                                                  @PathVariable String projectName) {

        return ResponseDto.success("파일 트리 보기 성공", projectService.fileTree(nickname, projectName));
    }

    @DeleteMapping("/{repoId}")
    public ResponseDto<Void> deleteRepo(@AuthenticationPrincipal LoginUser loginUser,
                                        @PathVariable Long repoId) {

        projectService.deleteRepo(loginUser.getUser().getNickname(), repoId);

        return ResponseDto.success("레포 삭제 성공", null);
    }

    @PutMapping("/{repoId}")
    public ResponseDto<Void> changeRepoName(@PathVariable Long repoId,
                                            @RequestBody ChangeRepoNameRequest changeRepoNameRequest) {

        projectService.changeRepoName(repoId, changeRepoNameRequest.getRepoName());

        return ResponseDto.success("레포 이름 수정 성공", null);
    }

    @GetMapping("/my")
    public ResponseDto<MyRepoListResponse> myRepoList(@AuthenticationPrincipal LoginUser loginUser) {

        return ResponseDto.success("내가 만든 레포 보기 성공", projectService.myRepoList(loginUser.getUser().getId()));
    }

    @GetMapping("/invited")
    public ResponseDto<InvitedRepoList> invitedRepoList(@AuthenticationPrincipal LoginUser loginUser) {

        return ResponseDto.success("초대된 레포 보기 성공", projectService.invitedRepoList(loginUser.getUser().getId()));
    }
}
