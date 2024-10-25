package in.ua.icetools.icedata.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "language_names", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "lang_id", columnList = "lang_id")
})
@Data
@NoArgsConstructor
public class LanguageName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "lang_id")
    private Long nameOwnerLangId;

    @Column(name = "name_lang_id")
    private int nameLangId;

    @Column(name = "name")
    private String name;

}
