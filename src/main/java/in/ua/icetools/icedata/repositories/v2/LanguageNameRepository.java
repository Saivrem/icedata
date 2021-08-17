package in.ua.icetools.icedata.repositories.v2;

import in.ua.icetools.icedata.models.v2.LanguageName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageNameRepository extends JpaRepository<LanguageName, Long> {
}
