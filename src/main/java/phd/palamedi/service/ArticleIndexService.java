package phd.palamedi.service;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import phd.palamedi.index.model.ArticleIndex;
import phd.palamedi.index.model.PublicationIndex;
import phd.palamedi.index.repository.ArticleIndexRepository;
import phd.palamedi.model.Article;
import phd.palamedi.model.Publication;
import phd.palamedi.repository.ArticleRepository;
import phd.palamedi.response.ArticleResponse;
import phd.palamedi.response.PublicationResponse;
import phd.palamedi.response.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

 /**
 * Created by marcos.salomao on 16/6/18.
 */
@Service
public class ArticleIndexService {

    private static final Logger LOGGER = Logger.getLogger(ArticleService.class.toString());

    @Autowired
    private ArticleIndexRepository articleIndexRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    public void indexAll() {

        ArticleIndex articleIndex;

        List<Article> articles = articleRepository.findAll();

        for (Article article : articles) {

            articleIndex = new ArticleIndex();
            copyProperties(articleIndex, article);

            PublicationIndex publicationIndex = new PublicationIndex();
            copyProperties(article.getPublication(), publicationIndex);

            articleIndex.setPublication(publicationIndex);

            LOGGER.info("Indexing article: " + article.getTitle());
            this.articleIndexRepository.save(articleIndex);
        }

    }

     public SearchResponse find(String search, Integer page, List<String> filters) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(search, filters.toArray(new String[filters.size()])))
                .withPageable(PageRequest.of(page, 10))
                .build();

        LOGGER.info(searchQuery.getQuery().toString());

        SearchResponse searchResponse = this.elasticsearchTemplate.query(
                searchQuery, new ResultsExtractor<SearchResponse>() {

            @Override
            public SearchResponse extract(org.elasticsearch.action.search.SearchResponse response) {

                ResultsMapper resultMapper = new DefaultResultMapper();
                List<ArticleResponse> articles = new ArrayList<>();

                for (SearchHit hit : response.getHits()) {
                    if (hit != null) {
                        try {
                            ArticleIndex articleIndex = resultMapper.getEntityMapper()
                                    .mapToObject(hit.getSourceAsString(), ArticleIndex.class);
                            ArticleResponse articleResponse = new ArticleResponse(articleIndex);
                            PublicationResponse publication = new PublicationResponse(articleIndex.getPublication());
                            articleResponse.setPublication(publication);
                            articles.add(articleResponse);
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, "Article not loaded " + hit.getId(), e);
                        }
                    }
                }

                SearchResponse searchResponse = new SearchResponse();

                searchResponse.setTotal(response.getHits().totalHits);
                searchResponse.setArticles(articles);

                return searchResponse;
            }

        });

        String query = searchQuery.getQuery().toString();
        searchResponse.setQuery(query);

        return searchResponse;
    }

    protected void copyProperties(ArticleIndex articleIndex, Article article) {
        BeanUtils.copyProperties(article, articleIndex);
        articleIndex.setId(article.getId().toString());
    }

     protected void copyProperties(Publication publication, PublicationIndex publicationIndex) {
         BeanUtils.copyProperties(publication, publicationIndex);
         publicationIndex.setPublisher(publication.getPublisher().getName());
     }

 }
