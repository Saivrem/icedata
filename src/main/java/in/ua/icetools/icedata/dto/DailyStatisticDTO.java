package in.ua.icetools.icedata.dto;

import in.ua.icetools.icedata.models.v1.DailyStatistic;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class DailyStatisticDTO {

    private final String langCode;
    private final String repository;
    private final String lastModified;
    private final boolean isAvailable;
    private final String type;

    public DailyStatisticDTO(DailyStatistic dailyStatistic) {
        langCode = dailyStatistic.getLangCode();
        repository = dailyStatistic.getRepository();
        isAvailable = dailyStatistic.getStatus().equals("good");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy :: HH:mm:ss");
        lastModified = sdf.format(dailyStatistic.getLastModified());
        this.type = dailyStatistic.getType();
    }
}
