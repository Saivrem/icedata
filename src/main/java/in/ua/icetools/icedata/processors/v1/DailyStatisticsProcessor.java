package in.ua.icetools.icedata.processors.v1;

import in.ua.icetools.icedata.models.v1.DailyStatistic;
import in.ua.icetools.icedata.models.v1.OldLanguage;
import in.ua.icetools.icedata.processors.HttpRequest;
import in.ua.icetools.icedata.repositories.v1.DailyStatisticRepository;
import in.ua.icetools.icedata.repositories.v1.OldLanguageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DailyStatisticsProcessor {

    final
    DailyStatisticRepository dailyStatisticRepository;
    final
    OldLanguageRepository oldLanguageRepository;

    public DailyStatisticsProcessor(DailyStatisticRepository dailyStatisticRepository, OldLanguageRepository oldLanguageRepository) {
        this.dailyStatisticRepository = dailyStatisticRepository;
        this.oldLanguageRepository = oldLanguageRepository;
    }


    public List<DailyStatistic> init() throws IOException {
        List<DailyStatistic> statistics = new ArrayList<>();

        dailyStatisticRepository.truncate();

        System.out.println("Here");
        for (URL link : getLinks()) {
            String[] list = link.toString().split("/");
            String lang = list[list.length - 2];
            String repo = list[list.length - 3];
            String type = list[list.length - 1].replaceAll("daily\\.index\\.", "");
            type = type.replaceAll("\\.gz", "");

            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            SimpleDateFormat tableFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            HttpRequest request = new HttpRequest(link);

            List<String> lastModified = request.headRequest().get("Last-Modified");

            DailyStatistic statistic = new DailyStatistic(lang, repo, type);

            Date inDate;
            try {
                inDate = inputFormat.parse(lastModified.get(0));
                System.out.printf("Asked %s%n", link.getFile());
                String currentTime = tableFormat.format(inDate);
                statistic.setLastModified(inDate);
                statistic.setStatus("good");
                System.out.printf("Processed %s\nDate: %s%n", link, currentTime);
            } catch (NullPointerException | ParseException e) {
                inDate = new Date();
                System.out.printf("Null value %s%n", link.getFile());
                statistic.setLastModified(inDate);
                statistic.setStatus("bad");
            }
            statistics.add(statistic);
        }

        return statistics;

    }

    private List<URL> getLinks() throws MalformedURLException {
        List<URL> result = new ArrayList<>();
        String[] repo = {"freexml", "level4"};
        String[] extension = {".xml.gz", ".csv.gz"};
        List<OldLanguage> allOldLanguages = oldLanguageRepository.findAll();

        for (OldLanguage lang : allOldLanguages) {
            for (String rep : repo) {
                for (String ext : extension) {
                    result.add(new URL(String.format("https://data.icecat.biz/export/%s/%s/daily.index%s",
                            rep,
                            lang.getCode(),
                            ext)));
                }
            }
        }

        return result;
    }
}
