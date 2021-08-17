package in.ua.icetools.icedata.controllers.v1;

import in.ua.icetools.icedata.dto.DailyStatisticDTO;
import in.ua.icetools.icedata.models.v1.DailyStatistic;
import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.processors.v1.DailyStatisticsProcessor;
import in.ua.icetools.icedata.repositories.v1.DailyStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("api/v1/statistic")
public class DailyStatisticController {

    @Autowired
    private DailyStatisticRepository dailyStatisticRepository;
    @Autowired
    private DailyStatisticsProcessor processor;

    @GetMapping("/month={month}/day={day}")
    public ResponseEntity<List<DailyStatisticDTO>> getStatistic(@Valid @PathVariable(value = "month") Integer month,
                                                                @Valid @PathVariable(value = "day") Integer day) {
        List<DailyStatistic> response = dailyStatisticRepository.getDailyStatistic(month, day);
        return getListResponseEntity(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<DailyStatisticDTO>> getAllStatistic() {
        List<DailyStatistic> data = dailyStatisticRepository.findAll();
        return getListResponseEntity(data);
    }

    private ResponseEntity<List<DailyStatisticDTO>> getListResponseEntity(List<DailyStatistic> response) {
        List<DailyStatisticDTO> resultList = new ArrayList<>();
        for (DailyStatistic dailyStatistic : response) {
            resultList.add(new DailyStatisticDTO(dailyStatistic));
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        return ResponseEntity.ok().headers(responseHeaders).body(resultList);
    }

    @PostMapping("/init")
    public ResponseEntity<String> init(@Valid @RequestBody Properties properties) {
        String response = "Done";

        String userName = properties.getProperty("userName");
        String passWord = properties.getProperty("passWord");

        Utils.authenticate(userName, passWord);

        try {

            dailyStatisticRepository.saveAll(processor.init());

        } catch (Exception e) {
            response = e.getMessage();
        }

        return ResponseEntity.ok().body(response);
    }

}
