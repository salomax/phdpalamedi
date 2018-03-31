package phd.palamedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import phd.palamedi.model.Article;
import phd.palamedi.model.Publisher;

import java.util.List;

/**
 * Created by marcos.salomao on 24/3/18.
 */
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Transactional
    void deleteByPublisher(Publisher publisher);

    List<Article> findByPublisher(Publisher publisher);
}
