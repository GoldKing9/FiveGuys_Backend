package fiveguys.webide.api.project.dto.request;

import lombok.Getter;

@Getter
public class FileCreateRequest {
    private String fileName;
    private String path;
}
