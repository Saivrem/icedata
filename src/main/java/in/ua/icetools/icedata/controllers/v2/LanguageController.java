package in.ua.icetools.icedata.controllers.v2;

import in.ua.icetools.icedata.models.v2.Language;
import in.ua.icetools.icedata.processors.Processor;
import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.processors.v2.LanguageProcessor;
import in.ua.icetools.icedata.repositories.v2.LanguageRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("api/v2/language")
public class LanguageController {

    private final LanguageRepository languageRepository;

    public LanguageController(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @PostMapping("/")
    public ResponseEntity<String> initLanguages(@Valid @RequestBody Properties properties) {
        String response = "Languages updated";

        Utils.authenticate(properties.getProperty("userName"), properties.getProperty("passWord"));
        Processor<Language> processor = new LanguageProcessor();

        try {
            languageRepository.saveAll(processor.process());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<Language>> getLanguages() {
        List<Language> languageList = languageRepository.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        return ResponseEntity.ok().headers(headers).body(languageList);
    }
}
