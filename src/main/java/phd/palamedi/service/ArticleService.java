package phd.palamedi.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phd.palamedi.model.Article;
import phd.palamedi.repository.ArticleRepository;
import phd.palamedi.response.ArticleResponse;
import phd.palamedi.response.SearchResponse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@Service
public class ArticleService {

    private static final Logger LOGGER = Logger.getLogger(ArticleService.class.toString());

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    public SearchResponse findByContent(String search) {

        SearchResponse searchResponse = new SearchResponse();
        StringBuilder query = new StringBuilder();
        List<String> terms = new ArrayList<>();

        query.append("SELECT a \nFROM Article a ");
        query.append("\nWHERE ");
        query.append("\n  a.status = 1 ");

        if (StringUtils.isNotEmpty(search)) {

            query.append("AND (\n\t");

            StringTokenizer st = new StringTokenizer(search);
            while (st.hasMoreTokens()) {

                String criteria = st.nextToken();
                if (criteria.equals("AND") || criteria.equals("OR")) {

                    query.append("\n\t");
                    query.append(criteria);
                    query.append(" ");

                } else {

                    terms.add(criteria);
                    query.append("a.content like '");
                    query.append("%");
                    query.append(criteria);
                    query.append("%");
                    query.append("'");

                }

            }

            query.append("\n  )");

        }

        LOGGER.info("Query: " + query.toString());

        List<Article> articles = this.entityManager.createQuery(query.toString(), Article.class).getResultList();

        searchResponse.setTerms(terms);
        searchResponse.setQuery(query.toString());
        searchResponse.setArticles(articles.stream().map(ArticleResponse::new).collect(Collectors.toList()));

        return searchResponse;
    }

    public Article get(Integer id) {

        Optional<Article> article = this.articleRepository.findById(id);

        if (article.isPresent()) {
            return article.get();
        }

        return null;
    }

    public void select(Integer id, Boolean selected) {

        Optional<Article> article = this.articleRepository.findById(id);

        if (article.isPresent()) {

            article.get().setSelected(selected);
            this.articleRepository.save(article.get());

        }

    }

}
