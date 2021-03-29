package in.ua.icetools.icedata.dto;

import lombok.Data;

@Data
public class LanguageDTO {

    private final long langId;
    private final String langCode;
    private final String langName;

    public LanguageDTO(long langId, String langCode, String langName) {
        this.langId = langId;
        this.langCode = langCode;
        this.langName = langName;
    }
}
