package in.ua.icetools.icedata.models.v2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "language", indexes = {
        @Index(name = "langId", columnList = "lang_id", unique = true)
})
@Data
@NoArgsConstructor
public class Language {

    @Id
    @Column(name = "lang_id")
    private Long langId;
    @Column(name = "int_lang_name")
    private String intLangName;
    @Column(name = "short_code")
    private String shortCode;
    @Column(name = "updated")
    private Date updated;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "target_lang_id", referencedColumnName = "lang_id")
    private List<LanguageName> names;
}
