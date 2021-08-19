package in.ua.icetools.icedata.controllers.v1;

import in.ua.icetools.icedata.dto.LanguageDTO;
import in.ua.icetools.icedata.models.v1.OldLanguageName;
import in.ua.icetools.icedata.models.v2.Language;
import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.processors.v1.OldLanguageProcessor;
import in.ua.icetools.icedata.repositories.v1.OldLanguageNameRepository;
import in.ua.icetools.icedata.repositories.v1.OldLanguageRepository;
import in.ua.icetools.icedata.repositories.v2.LanguageRepository;
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
public class OldLanguageController {

    @Autowired
    private OldLanguageRepository oldLanguageRepository;
    @Autowired
    private OldLanguageNameRepository oldLanguageNameRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @PostMapping("/init")
    public ResponseEntity<String> initLanguages(@Valid @RequestBody Properties properties) {
        String response = "Done";
        try {
            Utils.authenticate(properties.getProperty("userName"), properties.getProperty("passWord"));
            oldLanguageRepository.saveAll(OldLanguageProcessor.process(false, null));
            List<OldLanguageName> list = OldLanguageProcessor.getNamesList();
            oldLanguageNameRepository.saveAll(list);
        } catch (Exception e) {
            response = e.getMessage();
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<LanguageDTO>> getLanguageCodes() {
        List<LanguageDTO> result = new ArrayList<>();
        for (Language lang : languageRepository.findAll()) {
            LanguageDTO dto = new LanguageDTO(lang.getLangId(), lang.getShortCode(), lang.getIntLangName());
            result.add(dto);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        return ResponseEntity.ok().headers(headers).body(result);
    }
}
