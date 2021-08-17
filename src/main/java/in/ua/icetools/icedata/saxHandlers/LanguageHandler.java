package in.ua.icetools.icedata.saxHandlers;

import in.ua.icetools.icedata.models.v2.Language;
import in.ua.icetools.icedata.models.v2.LanguageName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LanguageHandler extends DefaultHandler {

    private final List<Language> languages = new ArrayList<>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Language language;
    private LanguageName name;
    private List<LanguageName> names = new ArrayList<>();

    public List<Language> getLanguages() {
        return languages;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName.toLowerCase()) {
            case "language":
                language = new Language();
                language.setLangId(Long.parseLong(attributes.getValue("ID")));
                language.setIntLangName(attributes.getValue("Code"));
                language.setShortCode(attributes.getValue("ShortCode"));
                language.setUpdated(getDate(attributes.getValue("Updated")));
                break;
            case "name":
                name = new LanguageName();
                name.setLangNameId(Long.parseLong(attributes.getValue("ID")));
                name.setTargetLangId(language.getLangId());
                name.setTranslationLangId(Long.parseLong(attributes.getValue("langid")));
                name.setNameTranslation(attributes.getValue("Value"));
                name.setUpdated(getDate(attributes.getValue("Updated")));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName.toLowerCase()) {
            case "language":
                language.setNames(names);
                names = new ArrayList<>();
                languages.add(language);
                break;
            case "name":
                names.add(name);
                break;
        }
    }

    private Date getDate(String updated) {
        try {
            return simpleDateFormat.parse(updated);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
}
