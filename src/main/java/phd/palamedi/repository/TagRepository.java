package phd.palamedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import phd.palamedi.model.ArticleContent;
import phd.palamedi.model.Tag;

import java.util.List;

/**
 * Created by marcos.salomao on 24/3/18.
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<Tag> findByUrl(String url);

    @Query("SELECT t.tag FROM Tag t WHERE t.url = ?1")
    List<String> findTagByUrl(String url);

    @Query("SELECT t.tag FROM Tag t GROUP BY t.tag")
    List<String> findAllTags();

    @Transactional
    @Modifying
    @Query("DELETE FROM Tag t WHERE t.url = ?1")
    void deleteByUrl(String url);
}
