package phd.palamedi.crawler.impl;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import phd.palamedi.crawler.ArticleCrawler;
import phd.palamedi.finder.ArticleVisitor;
import phd.palamedi.finder.VisitContext;
import phd.palamedi.finder.impl.ArticleFileContext;
import phd.palamedi.model.*;
import phd.palamedi.service.ErrorService;
import phd.palamedi.service.PublicationService;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by marcos.salomao on 13/12/17.
 */
@Component
public class OJSArticleCrawler implements ArticleCrawler {

    private static final Logger LOGGER = Logger.getLogger(OJSArticleCrawler.class.toString());

    private Set<String> filter = new HashSet<>();
    private Set<String> links = new HashSet<>();

    @Autowired
    private ArticleVisitor visitor;

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private ErrorService errorService;

    @Override
    public void execute(Publisher publisher) {

        this.links.clear();

        this.filter.addAll(publisher.getFilters().stream()
                .map(PublisherFilter::getUrl).collect(Collectors.toList()));

        publisher.getUrl().stream().forEach(url ->
                this.executor.execute(() -> searchPublications(publisher, url.getUrl()))
        );

    }

    protected List<Element> openURL(Publisher publisher, String url, String selector) {

        URLConnection urlConnection;
        try {

            if (!links.contains(url)) {

                // add it to the index
                links.add(url);

                // Open connection
                urlConnection = new URL(url).openConnection();
                if (urlConnection == null) {

                    String message = "Não foi possível conectar em '" + url + "'";
                    LOGGER.severe(message);
                    this.errorService.save(publisher, new ConnectException(message));

                } else {

                    // Parse the HTML
                    InputStream input = urlConnection.getInputStream();
                    Document document = Jsoup.parse(input, "UTF-8", url);

                    return document.select(selector);

                }

            }

        } catch (IOException e) {

            String message = "Erro acessar '" + url + "': " + e.getMessage();
            LOGGER.log(Level.SEVERE, message, e);
            this.errorService.save(publisher, message, e);

        }

        return new ArrayList<>();

    }


    protected void searchPublications(Publisher publisher, String url) {

        LOGGER.info("Searching publications " + url);

        List<Element> elements = this.openURL(publisher, url, "div[id=issues]");

        // Search by issues (summary)
        List<Element> issues = new ArrayList<>();
        for (Element element : elements) {
            issues.addAll(element.select("div[id^=issue]"));
        }

        // Search articles
        for (Element issue : issues) {
            this.executor.execute(() -> this.savePublication(issue, publisher));
        }

    }

    private String extractPublicationURL(Element issue) {
        return issue.selectFirst("a").attr("href") + "/showToc";
    }

    private String getArticleUrl(Element article) {

        String url = article.select(".tocTitle a").attr("href");

        // Check showToc url
        if (StringUtils.isEmpty(url)) {



        }

        return url;

    }

    protected void savePublication(Element issue, Publisher publisher) {

        String url = this.extractPublicationURL(issue);

        List<Element> elements = this.openURL(publisher, url, "body");

        List<Element> articles;

        for (Element element : elements) {

            LOGGER.info("Publication found " + url);

            Publication publication = new Publication();
            publication.setPublisher(publisher);
            publication.setUrl(url);

            List<Element> titles = element.select("h2,h3");
            for (Element title : titles) {

                Matcher matcher = Pattern.compile("20\\d{2}").matcher(title.text());
                if (matcher.find()) {
                    String year = matcher.group();
                    publication.setYear(Integer.valueOf(year));
                }

                publication.setName((publication.getName() == null ? "" : publication.getName() + " ") + title.text());
            }

            // fix name
            publication.setName(fixPublicationName(publication.getName()));

            this.publicationService.save(publication);

            articles = element.select("table.tocArticle");

            for (Element article : articles) {
                this.executor.execute(() -> this.saveArticle(article, publication));
            }

        }

    }

    private String fixPublicationName(String name) {

        if (StringUtils.isEmpty(name))
            return name;

        String newName = name.replaceAll("Sumário", "");

        if (newName.indexOf(")") >= 0) {
            newName = newName.substring(0, newName.indexOf(")") + 1);
        }

        return newName.trim();

    }

    protected void saveArticle(Element article, Publication publication) {

        // Extract article link
        String articleUrl = this.getArticleUrl(article);

        if (StringUtils.isNotEmpty(articleUrl)) {

            LOGGER.info("Article found " + articleUrl);

            this.saveArticle(articleUrl, publication);

        } else {

            String message = "Artigo não encontrado '"
                    + article.text()
                    + " ' Publicação '"
                    + publication.getName()
                    + "' url '"
                    + publication.getUrl()
                    + "'";

            LOGGER.log(Level.SEVERE, message);
            this.errorService.save(publication.getPublisher(), message);

        }

    }

    protected void saveArticle(String articleUrl, Publication publication) {

        List<Element> articles = this.openURL(publication.getPublisher(), articleUrl, "div[id=content]");

        for (Element element : articles) {

            final Article article = new Article();
            article.setPublication(publication);

            article.setTitle(getText(element, "div[id=articleTitle]"));
            article.setSummary(removePrefix(getText(element, "div[id=articleAbstract]"), "Resumo"));
            article.setAuthor(getText(element, "div[id=authorString]"));
            article.setKeywords(removePrefix(getText(element, "div[id=articleSubject]"), "Palavras-chave"));

            URLConnection pdf = this.findPdfLink(element);
            if (pdf != null) {

                ArticleFileConnection articleFile = new ArticleFileConnection(pdf);

                VisitContext context = new ArticleFileContext(article, articleFile);
                this.visitor.visit(context);

            } else {

                String message = " PDF não encontrado. Article '"
                        + article.getTitle()
                        + "' url "
                        + articleUrl
                        + " ' Publicacão '"
                        + publication.getName()
                        + "' url '"
                        + publication.getUrl()
                        + "'";

                LOGGER.log(Level.SEVERE, message);
                this.errorService.save(publication.getPublisher(), message);

            }

        }

    }

    private String removePrefix(final String text, final String prefix) {

        if (StringUtils.isEmpty(text))
            return text;

        String newText = text;
        if (text.startsWith(prefix)) {
            newText = newText.substring(prefix.length(), newText.length());
        }

        return newText.trim();
    }

    private String getText(Element element, String selector) {
        Optional<Element> text = Optional.ofNullable(element.selectFirst(selector));
        if (text.isPresent()) {
            return text.get().text();
        }
        return "";
    }

    private URLConnection findPdfLink(Element element) {

        String pdfUrl = null;

        // Search for PDF download
        List<Element> links = element.select("a");
        for (Element link : links) {
            if ("PDF".equals(link.text())) {
                pdfUrl = link.attr("href").replaceAll("article/view", "article/download");
                break;
            }
        }

        try {
            return new URL(pdfUrl).openConnection();
        } catch (IOException e) {
            return null;
        }

    }

}
