package in.ua.icetools.icedata.resources;

import in.ua.icetools.icedata.models.LanguageName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageNameRepository extends JpaRepository<LanguageName, Long> {
}
