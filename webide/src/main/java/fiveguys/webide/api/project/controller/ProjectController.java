package fiveguys.webide.api.project.controller;

import fiveguys.webide.api.project.dto.request.FileCreateRequest;
import fiveguys.webide.api.project.dto.request.FolderCreateRequest;
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

}
