package phd.palamedi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phd.palamedi.model.Article;
import phd.palamedi.model.ArticleContent;
import phd.palamedi.model.Publisher;
import phd.palamedi.repository.ArticleContentRepository;

import javax.persistence.EntityManager;

/**
 * Created by marcos.salomao on 4/4/18.
 */
@Service
public class ArticleContentService {

    @Autowired
    private ArticleContentRepository articleContentRepository;

    @Autowired
    private EntityManager entityManager;

    public void save(ArticleContent articleContent) {
        this.articleContentRepository.save(articleContent);
    }

    public ArticleContent get(Integer id) {
        return this.articleContentRepository.getOne(id);
    }

    @Transactional
    public void deleteByPublisher(Publisher publisher) {

        StringBuilder deleteQuery = new StringBuilder();

        deleteQuery.append("DELETE  ac.* ");
        deleteQuery.append("FROM tb_article_content ac ");
        deleteQuery.append("    INNER JOIN tb_article a ON ac.article_id = a.id ");
        deleteQuery.append("    INNER JOIN tb_publication pc ON a.publication_id = pc.id ");
        deleteQuery.append("    INNER JOIN tb_publisher p ON pc.publisher_id = p.id ");
        deleteQuery.append("WHERE p.id = ?1 ");

        this.entityManager.createNativeQuery(deleteQuery.toString())
            .setParameter(1, publisher.getId()).executeUpdate();

    }

    public ArticleContent getByArticleAndUrl(Article article, String articleDownloadUrl) {
        return this.articleContentRepository.getByArticleAndUrl(article, articleDownloadUrl);
    }
}
