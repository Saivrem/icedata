package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.Language;
import in.ua.icetools.icedata.models.LanguageName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static in.ua.icetools.icedata.constants.RepositoryLinks.LANGUAGES_LIST_URL;
import static in.ua.icetools.icedata.processors.Utils.readAttribute;

public class LanguageProcessor {

    private static final List<LanguageName> namesList = new ArrayList<>();

    public static List<LanguageName> getNamesList() {
        return namesList;
    }

    public static List<Language> process(boolean test, File testFile) throws Exception {

        //todo  move such code parts into some method "prepareFile"
        File resultFile = test ? testFile : new File("unzippedLanguagesListFile.tmp");
        resultFile.delete();
        if (!test) {
            File zippedLanguagesFile = new File("gzippedLanguagesFile.tmp");
            zippedLanguagesFile.delete();
            Utils.downloadURL(LANGUAGES_LIST_URL, zippedLanguagesFile);
            Utils.unGzip(zippedLanguagesFile, resultFile);

        }

        List<Language> result = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(resultFile));
        Language currentLanguage = new Language();
        Map<String, String> attributes;

        while (reader.ready()) {
            String line = reader.readLine().trim();
            if (line.startsWith("<!")) {
                continue;
            }
            attributes = readAttribute(line);
            switch (attributes.get("tag")) {
                case "Language":
                    currentLanguage.setCode(attributes.get("ShortCode"));
                    currentLanguage.setName(attributes.get("Code"));
                    currentLanguage.setLangId(Long.parseLong(attributes.get("ID")));
                    break;
                case "Name":
                    LanguageName name = new LanguageName();
                    name.setName(attributes.get("Value"));
                    name.setNameLangId(Integer.parseInt(attributes.get("langid")));
                    name.setNameOwnerLangId(currentLanguage.getLangId());
                    namesList.add(name);
                    break;
                case "/Language>":
                    result.add(currentLanguage);
                    currentLanguage = new Language();
                    break;
            }
        }

        return result;
    }

}
