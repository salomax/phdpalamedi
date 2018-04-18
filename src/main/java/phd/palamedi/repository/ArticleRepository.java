package phd.palamedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phd.palamedi.model.Article;
import phd.palamedi.model.Publication;
import phd.palamedi.model.Publisher;

import java.util.List;

/**
 * Created by marcos.salomao on 24/3/18.
 */
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query("SELECT a FROM Article a INNER JOIN Publication p ON a.publication = p WHERE p.publisher = ?1")
    List<Article> findByPublisher(Publisher publisher);

    Article getByPublicationAndTitle(Publication publication, String articleTitle);
}
