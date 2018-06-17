package phd.palamedi.index.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import phd.palamedi.index.model.ArticleIndex;

/**
 * Created by marcos.salomao on 16/6/18.
 */
public interface ArticleIndexRepository extends ElasticsearchRepository<ArticleIndex, String> {
}
