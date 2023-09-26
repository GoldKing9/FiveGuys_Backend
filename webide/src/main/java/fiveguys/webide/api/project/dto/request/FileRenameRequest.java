package fiveguys.webide.api.project.dto.request;

import lombok.Getter;

@Getter
public class FileRenameRequest {
    private String fileName;
    private String path;
}
