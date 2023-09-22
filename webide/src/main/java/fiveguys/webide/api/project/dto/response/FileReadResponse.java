package fiveguys.webide.api.project.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FileReadResponse {
    private String body;
    private int lines;
    private int size;

    @Builder
    public FileReadResponse(String body, int lines, int size){
        this.body = body;
        this.lines = lines;
        this.size = size;
    }

}
