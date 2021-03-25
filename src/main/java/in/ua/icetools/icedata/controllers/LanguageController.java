package in.ua.icetools.icedata.controllers;

import in.ua.icetools.icedata.dto.LanguageDTO;
import in.ua.icetools.icedata.models.Language;
import in.ua.icetools.icedata.models.LanguageName;
import in.ua.icetools.icedata.processors.LanguageProcessor;
import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.resources.LanguageNameRepository;
import in.ua.icetools.icedata.resources.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("api/v1/language")
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private LanguageNameRepository languageNameRepository;

    @PostMapping("/init")
    public ResponseEntity<String> initLanguages(@Valid @RequestBody Properties properties) {
        String response = "Done";
        try {
            Utils.authenticate(properties.getProperty("userName"), properties.getProperty("passWord"));
            languageRepository.saveAll(LanguageProcessor.process(false, null));
            List<LanguageName> list = LanguageProcessor.getNamesList();
            languageNameRepository.saveAll(list);
        } catch (Exception e) {
            response = e.getMessage();
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<LanguageDTO>> getLanguageCodes() {
        List<Language> languages = languageRepository.findAll();
        List<LanguageDTO> result = new ArrayList<>();
        for (Language language : languages) {
            result.add(new LanguageDTO(language.getLangId(), language.getCode(), language.getName()));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        return ResponseEntity.ok().headers(headers).body(result);
    }
}
