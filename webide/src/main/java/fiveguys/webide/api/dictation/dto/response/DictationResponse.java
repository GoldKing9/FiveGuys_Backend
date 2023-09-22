package fiveguys.webide.api.dictation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DictationResponse {
    private String body;

    public DictationResponse(String body) {
        this.body = body;
    }
}
