package fiveguys.webide.api.project.dto.request;

import lombok.Getter;

@Getter
public class FolderRenameRequest {
    private String folderName;
    private String path;
}
