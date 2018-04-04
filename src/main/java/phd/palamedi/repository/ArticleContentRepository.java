package phd.palamedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phd.palamedi.model.Article;
import phd.palamedi.model.ArticleContent;
import phd.palamedi.model.Publisher;

import java.util.List;

/**
 * Created by marcos.salomao on 24/3/18.
 */
public interface ArticleContentRepository extends JpaRepository<ArticleContent, Integer> {

}
