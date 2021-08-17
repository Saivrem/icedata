package in.ua.icetools.icedata.models.v2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "language_names")
@Data
@NoArgsConstructor
public class LanguageName {

    @Id
    @Column(name = "lang_name_id")
    private Long langNameId;

    @Column(name = "translation_lang_id")
    private Long translationLangId;

    @Column(name = "target_lang_id")
    private Long targetLangId;

    @Column(name = "name_translation")
    private String nameTranslation;

    @Column(name = "updated")
    private Date updated;

}
