package in.ua.icetools.icedata.controllers;

import in.ua.icetools.icedata.dto.DailyStatisticDTO;
import in.ua.icetools.icedata.models.DailyStatistic;
import in.ua.icetools.icedata.resources.DailyStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/statistic")
public class DailyStatisticController {

    @Autowired
    private DailyStatisticRepository dailyStatisticRepository;

    @GetMapping("/month={month}/day={day}/repo={repo}")
    public ResponseEntity<List<DailyStatisticDTO>> getStatistic(@Valid @PathVariable(value = "month") Integer month,
                                                                @Valid @PathVariable(value = "day") Integer day,
                                                                @Valid @PathVariable(value = "repo") String repo) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        //Integer today = Integer.parseInt(sdf.format(new Date()));

        List<DailyStatistic> response = dailyStatisticRepository.getDailyStatistic(month, day, repo);
        List<DailyStatisticDTO> resultList = new ArrayList<>();
        for (DailyStatistic dailyStatistic : response) {
            resultList.add(new DailyStatisticDTO(dailyStatistic));
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.ok().headers(responseHeaders).body(resultList);
    }

}
