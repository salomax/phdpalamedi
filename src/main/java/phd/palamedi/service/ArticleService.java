package phd.palamedi.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phd.palamedi.exception.AcademicsException;
import phd.palamedi.model.Article;
import phd.palamedi.model.ArticleContent;
import phd.palamedi.model.Publication;
import phd.palamedi.model.Tag;
import phd.palamedi.repository.ArticleRepository;
import phd.palamedi.response.ArticleContentResponse;
import phd.palamedi.response.ArticleResponse;
import phd.palamedi.response.SearchResponse;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@Service
public class ArticleService {

    private static final Logger LOGGER = Logger.getLogger(ArticleService.class.toString());
    private static final Integer ROWS_PAGE = 100;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagService tagService;

    public SearchResponse findByTags(List<String> tags) {

        SearchResponse searchResponse = new SearchResponse();
        StringBuilder query = new StringBuilder();

        query.append("\nSELECT ac ");
        query.append("\nFROM ArticleContent ac, ");
        query.append("\n\tArticle a, ");
        query.append("\n\tTag t ");
        query.append("\nWHERE ");
        query.append("\n  ac.status = 0 ");
        query.append("\n  AND ac.article = a ");
        query.append("\n  AND a.url = t.url ");
        query.append("\n  AND t.tag IN :tags");

        LOGGER.info("Query: " + query.toString());

        List<ArticleContent> articleContents = this.entityManager.createQuery(
                query.toString(), ArticleContent.class).setParameter("tags", tags)
                .getResultList();

        return getSearchResponse(query, tags, articleContents, articleContents.size());
    }


    public SearchResponse findByContent(String search, Integer page) throws AcademicsException {

        StringBuilder query = new StringBuilder();

        query.append("\nSELECT tb_article_content.id, tb_article_content.status, tb_article_content.url, tb_article_content.article_id, NULL as content \n");
        query.append("FROM tb_article \n");
        query.append("  INNER JOIN tb_article_content \n");
        query.append("      ON tb_article.id = tb_article_content.article_id ");
        query.append("WHERE \n");
        query.append("  tb_article_content.status = 0 AND ( \n");

        query.append("        MATCH(tb_article_content.content) AGAINST('");
        query.append(search);
        query.append("' IN BOOLEAN MODE) \n");

        query.append("        OR MATCH(tb_article.title, tb_article.author, tb_article.summary, tb_article.keywords) AGAINST('");
        query.append(search);
        query.append("' IN BOOLEAN MODE) \n");

        query.append(") \n");

        LOGGER.info("Query: " + query.toString());

        List<ArticleContent> result = this.entityManager.createNativeQuery(
                query.toString(), ArticleContent.class).getResultList();

        Set<String> temp = new HashSet<>();
        StringTokenizer token = new StringTokenizer(search.replaceAll("[\\*\\+\\-~()]", ""));
        while (token.hasMoreTokens()) {
            temp.add(token.nextToken());
        }

        List<String> terms = new ArrayList<>();
        terms.addAll(temp);

        List<ArticleContent> resultPage = getPage(result, page);

        return getSearchResponse(query, terms, resultPage, result.size());
    }

    private List<ArticleContent> getPage(List<ArticleContent> result, Integer page) throws AcademicsException {

        if (((double) result.size()/ROWS_PAGE) > 1.0) {

            int from = 0 + ((page - 1) * ROWS_PAGE);
            int to = from + ROWS_PAGE;

            if (to > result.size()) {
                to = result.size();
            }

            try {

                return result.subList(from, to);
            } catch (IndexOutOfBoundsException e) {
                throw new AcademicsException("Page not found " + page, e);
            }


        } else {

            return result;

        }

    }

    private SearchResponse getSearchResponse(
            StringBuilder query, List<String> terms, List<ArticleContent> articleContents, Integer total) {

        SearchResponse searchResponse = new SearchResponse();

        searchResponse.setTotal(total);
        searchResponse.setTerms(terms);
        searchResponse.setQuery(query.toString());

        Map<Integer, ArticleResponse> articles = new LinkedHashMap<>();

        for (ArticleContent articleContent : articleContents) {

            ArticleResponse articleResponse;
            if (!articles.containsKey(articleContent.getArticle().getId())) {
                articleResponse = new ArticleResponse(articleContent.getArticle());
                articles.put(articleContent.getArticle().getId(), articleResponse);
            } else {
                articleResponse = articles.get(articleContent.getArticle().getId());
            }

            articleResponse.setTags(this.tagService.listTagByUrl(articleResponse.getUrl()));

            articleResponse.getArticleContents().add(new ArticleContentResponse(articleContent));
        }

        searchResponse.setArticles(articles.values().stream().collect(Collectors.toList()));

        searchResponse.setTags(this.tagService.findAllTags());

        return searchResponse;
    }

    public Article get(Integer id) {

        Optional<Article> article = this.articleRepository.findById(id);

        if (article.isPresent()) {
            return article.get();
        }

        return null;
    }

    @Transactional
    public void save(Article article) {
        this.articleRepository.save(article);
    }

    public Article getByPublicationAndTitle(Publication publication, String articleTitle) {
        return this.articleRepository.getByPublicationAndTitle(publication, articleTitle);
    }
}
