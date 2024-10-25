package in.ua.icetools.icedata.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "languages", indexes = {
        @Index(name = "languageId", columnList = "lang_id", unique = true)
})
@Data
@NoArgsConstructor
public class Language {

    @Id
    @Column(name = "lang_id")
    private Long langId;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @OneToMany
    //first is column in target table, second is column in this table
    @JoinColumn(name = "lang_id", referencedColumnName = "lang_id")
    private List<LanguageName> names;

}
