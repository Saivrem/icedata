package in.ua.icetools.icedata.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "daily_statistics")
@Data
@NoArgsConstructor
public class DailyStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;
    @Column(name = "lang_code")
    private String langCode;
    @Column(name = "repo")
    private String repository;
    @Column(name = "last_modified")
    private Date lastModified;
    @Column(name = "flag", columnDefinition = " varchar(10) default 'good'")
    private String status;
    @Column(name = "type")
    private String type;

    public DailyStatistic(String langCode, String repository, String type) {
        this.langCode = langCode;
        this.repository = repository;
        this.type = type;
    }
}
