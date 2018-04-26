package phd.palamedi.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import phd.palamedi.finder.ArticleVisitor;
import phd.palamedi.finder.VisitContext;
import phd.palamedi.finder.impl.ArticleFileContext;
import phd.palamedi.model.*;
import phd.palamedi.service.ArticleContentService;
import phd.palamedi.service.ArticleService;
import phd.palamedi.service.PublicationService;
import phd.palamedi.service.PublisherService;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by marcos.salomao on 18/4/18.
 */
@Service
public class ArticleImporter {

    private static final Logger LOGGER = Logger.getLogger(JSONLoaderController.class.toString());

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleContentService articleServiceService;

    @Autowired
    private ArticleVisitor visitor;

    @Autowired
    private TaskExecutor executor;

    public void load(ArticleLoader articleLoader) {

        LOGGER.info(articleLoader.getArticleTitle());

        Publisher publisher = getPublisher(articleLoader);

        Publication publication = getPublication(publisher, articleLoader);

        Article article = getArticle(publication, articleLoader);

        if (article != null) {
            this.executor.execute(() ->
                loadArticleContents(article, articleLoader));
        }

    }

    private Publisher getPublisher(ArticleLoader articleLoader) {
        String publisherName = articleLoader.getPublisher();
        Publisher publisher = this.publisherService.getByName(publisherName);

        if (publisher == null) {
            publisher = new Publisher();
            publisher.setName(publisherName);

            this.publisherService.save(publisher);

            LOGGER.info("Novo periódico criado: " + publisherName);
        }

        return publisher;

    }

    private Publication getPublication(Publisher publisher, ArticleLoader articleLoader) {

        Publication publication = this.publicationService
                .getByPublisherAndName(publisher, articleLoader.getPublicationName());

        if (publication == null) {
            publication = new Publication();
            publication.setPublisher(publisher);
            publication.setName(articleLoader.getPublicationName());
            publication.setYear(articleLoader.getPublicationYear());
            publication.setUrl(articleLoader.getPublicationUrl());

            this.publicationService.save(publication);

            LOGGER.info("Nova publicação criada: " + articleLoader.getPublicationName());
        }

        return publication;

    }

    private Article getArticle(Publication publication, ArticleLoader articleLoader) {

        if ("n".equals(articleLoader.getArticleTitle())) {
            LOGGER.severe("Artigo não carregado: " + articleLoader.getArticleTitle());
            return null;
        }

        Article article = this.articleService
                .getByPublicationAndTitle(publication, articleLoader.getArticleTitle());

        if (article == null) {
            article = new Article();
            article.setPublication(publication);
            article.setUrl(articleLoader.getArticleUrl());
            article.setTitle(articleLoader.getArticleTitle());
            article.setUrl(articleLoader.getArticleUrl());
            article.setKeywords(articleLoader.getArticleKeywords());
            article.setAuthor(articleLoader.getArticleAuthor());
            article.setSummary(articleLoader.getArticleAbstract());
            article.setCreated(new Date());

            this.articleService.save(article);

            LOGGER.info("Novo Artigo criado: " + articleLoader.getArticleTitle());
        }

        return article;

    }

    private void loadArticleContents(Article article, ArticleLoader articleLoader) {

        ArticleContent articleContent = this.articleServiceService
                .getByArticleAndUrl(article, articleLoader.getArticleDownloadUrl());

        if (articleContent == null) {

            LOGGER.info("Carregando artigo " + articleLoader.getArticleDownloadUrl());

            List<URLConnection> connections = new ArrayList<>();

            URLConnection connection = null;
            try {

                connection = new URL(articleLoader.getArticleDownloadUrl()).openConnection();

                connection.addRequestProperty("User-Agent", "Mozilla/4.76");
                connections.add(connection);

                ArticleFileConnection articleFile = new ArticleFileConnection(connections);
                VisitContext context = new ArticleFileContext(article, articleFile);
                this.visitor.visit(context);

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Artigo não carregado " + articleLoader.getArticleDownloadUrl(), e);
            }

        }

    }


}
