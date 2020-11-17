package in.ua.icetools.icedata.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "daily_statistics")
@Data
@NoArgsConstructor
public class DailyStatistic {

    @Id
    @Column(name = "id", unique = true)
    private Long id;
    @Column(name = "lang_code")
    private String langCode;
    @Column(name = "repo")
    private String repository;
    @Column(name = "last_modified")
    private Date lastModified;
    @Column(name = "flag")
    private String status;

}
