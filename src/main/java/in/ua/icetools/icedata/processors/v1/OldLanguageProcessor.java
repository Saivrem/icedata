package in.ua.icetools.icedata.processors.v1;

import in.ua.icetools.icedata.models.v1.OldLanguage;
import in.ua.icetools.icedata.models.v1.OldLanguageName;
import in.ua.icetools.icedata.processors.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static in.ua.icetools.icedata.constants.RepositoryLinks.LANGUAGES_LIST_URL;
import static in.ua.icetools.icedata.processors.Utils.readAttribute;

public class OldLanguageProcessor {

    private static final List<OldLanguageName> namesList = new ArrayList<>();

    public static List<OldLanguageName> getNamesList() {
        return namesList;
    }

    public static List<OldLanguage> process(boolean test, File testFile) throws Exception {

        //todo  move such code parts into some method "prepareFile"
        File resultFile = test ? testFile : new File("unzippedLanguagesListFile.tmp");
        resultFile.delete();
        if (!test) {
            File zippedLanguagesFile = new File("gzippedLanguagesFile.tmp");
            zippedLanguagesFile.delete();
            Utils.oldDownloadURL(LANGUAGES_LIST_URL, zippedLanguagesFile);
            Utils.oldUnGzip(zippedLanguagesFile, resultFile);

        }

        List<OldLanguage> result = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(resultFile));
        OldLanguage currentOldLanguage = new OldLanguage();
        Map<String, String> attributes;

        while (reader.ready()) {
            String line = reader.readLine().trim();
            if (line.startsWith("<!")) {
                continue;
            }
            attributes = readAttribute(line);
            switch (attributes.get("tag")) {
                case "Language":
                    currentOldLanguage.setCode(attributes.get("ShortCode"));
                    currentOldLanguage.setName(attributes.get("Code"));
                    currentOldLanguage.setLangId(Long.parseLong(attributes.get("ID")));
                    break;
                case "Name":
                    OldLanguageName name = new OldLanguageName();
                    name.setName(attributes.get("Value"));
                    name.setNameLangId(Integer.parseInt(attributes.get("langid")));
                    name.setNameOwnerLangId(currentOldLanguage.getLangId());
                    namesList.add(name);
                    break;
                case "/Language>":
                    result.add(currentOldLanguage);
                    currentOldLanguage = new OldLanguage();
                    break;
            }
        }

        return result;
    }

}
