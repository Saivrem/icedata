package in.ua.icetools.icedata.repositories.v1;

import in.ua.icetools.icedata.models.v1.OldLanguageName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OldLanguageNameRepository extends JpaRepository<OldLanguageName, Long> {
}
