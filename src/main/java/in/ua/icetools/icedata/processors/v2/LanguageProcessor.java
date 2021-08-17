package in.ua.icetools.icedata.processors.v2;

import in.ua.icetools.icedata.models.v2.Language;
import in.ua.icetools.icedata.processors.Processor;
import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.saxHandlers.LanguageHandler;
import org.springframework.stereotype.Service;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static in.ua.icetools.icedata.constants.RepositoryLinks.LANGUAGES_LIST_URL;

@Service
public class LanguageProcessor implements Processor<Language> {

    private final Path unzippedFile;
    private final Path zippedFile;

    public LanguageProcessor() {
        unzippedFile = Paths.get("LanguagesList.xml");
        zippedFile = Paths.get("LanguagesList.xml.gz");

        try {
            Files.deleteIfExists(unzippedFile);
            Files.deleteIfExists(zippedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Language> process() throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        Utils.downloadUrl(LANGUAGES_LIST_URL, zippedFile);
        Utils.unGzip(zippedFile, unzippedFile);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        LanguageHandler handler = new LanguageHandler();
        saxParser.parse(unzippedFile.toFile(), handler);

        return handler.getLanguages();
    }
}
