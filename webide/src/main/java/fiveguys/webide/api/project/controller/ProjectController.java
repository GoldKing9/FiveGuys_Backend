package fiveguys.webide.api.project.controller;

import fiveguys.webide.api.project.dto.request.*;
import fiveguys.webide.api.project.dto.response.FileReadResponse;
import fiveguys.webide.api.project.service.ProjectService;
import fiveguys.webide.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/refile/tree/{*path}")
    public ResponseDto<Void> fileRename(@RequestBody FileRenameRequest fileRenameRequest, @PathVariable String path){
        projectService.fileRename(fileRenameRequest, path);
        return ResponseDto.success("파일명 수정 성공",null);
    }
    @PutMapping("/folder/tree/{*path}")
    public ResponseDto<Void> folderRename(@RequestBody FolderRenameRequest folderRenameRequest, @PathVariable String path){
        projectService.folderRename(folderRenameRequest, path);
        return ResponseDto.success("폴더명 수정 성공", null);
    }
    @PutMapping("/file/tree/{*path}")
    public ResponseDto<Void> fileChangeBody(@RequestBody FileNewBodyRequest fileNewBodyRequest, @PathVariable String path){
        projectService.fileChangeBody(fileNewBodyRequest, path);
        return ResponseDto.success("폴더내용 수정 성공", null);
    }
}
