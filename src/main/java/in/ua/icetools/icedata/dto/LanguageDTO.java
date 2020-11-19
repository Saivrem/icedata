package in.ua.icetools.icedata.dto;

import lombok.Data;

@Data
public class LanguageDTO {

    private final String langCode;
    private final String langName;

    public LanguageDTO(String langCode, String langName) {
        this.langCode = langCode;
        this.langName = langName;
    }
}
