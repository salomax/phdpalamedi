package phd.palamedi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phd.palamedi.crawler.ArticleCrawler;
import phd.palamedi.model.Article;
import phd.palamedi.model.Publication;
import phd.palamedi.model.Publisher;
import phd.palamedi.model.PublisherDetails;
import phd.palamedi.repository.ArticleRepository;
import phd.palamedi.repository.PublicationRepository;
import phd.palamedi.repository.PublisherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by marcos.salomao on 24/3/18.
 */
@Service
public class PublisherService {

    private static final Logger LOGGER = Logger.getLogger(PublisherService.class.toString());

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private ArticleCrawler articleCrawler;

    @Autowired
    private ErrorService errorService;

    public List<Publisher> findAll() {
        return this.publisherRepository.findAll();
    }

    public void loadPublisher(Integer id) {

        final Optional<Publisher> publisher = this.publisherRepository.findById(id);

        if (publisher.isPresent()) {

            LOGGER.info("Get started to load articles from publisher " + publisher.get().getName());

            // Clean up all articles from the publisher
            this.publicationRepository.deleteByPublisher(publisher.get());
            this.errorService.deleteByPublisher(publisher.get());

            LOGGER.info("Removed all articles from publisher " + publisher.get().getName());

            this.articleCrawler.execute(publisher.get());

        }

    }

    public PublisherDetails getDetails(Integer id) {

        PublisherDetails details = new PublisherDetails();

        final Optional<Publisher> publisher = this.publisherRepository.findById(id);

        if (publisher.isPresent()) {

            List<Article>  articles = this.articleRepository.findByPublisher(publisher.get());

            articles.stream().forEach(article -> {

                details.setTotalArticles(details.getTotalArticles() + 1);

                switch (article.getStatus()) {
                    case OK:
                        details.setTotalArticlesSuccess(details.getTotalArticlesSuccess() + 1);
                        break;
                    default:
                        details.setTotalArticlesError(details.getTotalArticlesError() + 1);
                        break;
                }

            });


        }

        return details;

    }
}
