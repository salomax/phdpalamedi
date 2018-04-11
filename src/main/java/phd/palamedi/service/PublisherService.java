package phd.palamedi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phd.palamedi.crawler.ArticleCrawler;
import phd.palamedi.model.*;
import phd.palamedi.repository.ArticleRepository;
import phd.palamedi.repository.PublicationRepository;
import phd.palamedi.repository.PublisherRepository;
import phd.palamedi.response.PublisherResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    @Autowired
    private ArticleContentService articleContentService;

    public List<Publisher> findAll() {
        return this.publisherRepository.findAll();
    }

    public void loadPublisher(Integer id) {

        final Optional<Publisher> publisher = this.publisherRepository.findById(id);

        if (publisher.isPresent()) {

            LOGGER.info("Get started to load articles from publisher " + publisher.get().getName());

            // Clean up all articles from the publisher
            this.articleContentService.deleteByPublisher(publisher.get());
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

            List<Article> articles = this.articleRepository.findByPublisher(publisher.get());
            details.setTotalArticles(articles.size());

            Integer countErrors = this.errorService.countByPublisher(publisher.get());
            details.setTotalErrors(countErrors);

        }

        return details;

    }
}
