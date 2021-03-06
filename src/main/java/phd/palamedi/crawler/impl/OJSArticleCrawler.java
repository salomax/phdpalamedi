package phd.palamedi.crawler.impl;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import phd.palamedi.crawler.ArticleCrawler;
import phd.palamedi.crawler.OJSSelector;
import phd.palamedi.finder.ArticleVisitor;
import phd.palamedi.finder.VisitContext;
import phd.palamedi.finder.impl.ArticleFileContext;
import phd.palamedi.model.*;
import phd.palamedi.service.ArticleService;
import phd.palamedi.service.ErrorService;
import phd.palamedi.service.PublicationService;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
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
    private ArticleService articleService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private OJSSelectorFactory selectorFactory;


    public OJSArticleCrawler() {
        setupSSL();
    }

    private void setupSSL() {

        LOGGER.info("Setting up SSL connections");

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };

        try {

            // Install the all-trusting trust manager
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SSL connections not setup", e);
        }

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                // TODO load all urls from publish and extract their localhost
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    }

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
                urlConnection.addRequestProperty("User-Agent", "Mozilla/4.76");

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

        OJSSelector selector = this.selectorFactory.get(publisher);

        List<Element> elements = this.openURL(publisher, url, selector.getIssuesArchive());

        // Search by issues (summary)
        List<Element> issues = new ArrayList<>();
        for (Element element : elements) {
            issues.addAll(element.select(selector.getIssues()));
        }

        // Search articles
        for (Element issue : issues) {
            this.executor.execute(() -> this.savePublication(issue, publisher, selector));
        }

    }

    private String extractPublicationURL(Element issue) {
        return issue.selectFirst("a").attr("href") + "/showToc";
    }

    private String getArticleUrl(Element article, OJSSelector selector) {
        return article.select(selector.getArticleUrl()).attr("href");
    }

    protected void savePublication(Element issue, Publisher publisher, OJSSelector selector) {

        String url = this.extractPublicationURL(issue);

        List<Element> elements = this.openURL(publisher, url, selector.getPublicationContent());

        List<Element> articles;

        for (Element element : elements) {

            LOGGER.info("Publication found " + url);

            Publication publication = new Publication();
            publication.setPublisher(publisher);
            publication.setUrl(url);

            List<Element> titles = element.select(selector.getPublicationTitle());
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

            articles = element.select(selector.getArticles());

            for (Element article : articles) {
                this.executor.execute(() -> this.saveArticle(article, publication, selector));
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

    protected void saveArticle(Element article, Publication publication, OJSSelector selector) {

        // Extract article link
        String articleUrl = this.getArticleUrl(article, selector);

        if (StringUtils.isNotEmpty(articleUrl)) {

            LOGGER.info("Article found " + articleUrl);

            this.saveArticle(articleUrl, publication, selector);

        } else {

            String message = "Artigo não encontrado '"
                    + article.text()
                    + "'.\nPublicação '"
                    + publication.getName()
                    + "'\nURL '"
                    + publication.getUrl()
                    + "'";

            LOGGER.log(Level.SEVERE, message);
            this.errorService.save(publication.getPublisher(), message);

        }

    }

    protected void saveArticle(String articleUrl, Publication publication, OJSSelector selector) {

        List<Element> articles = this.openURL(publication.getPublisher(), articleUrl, selector.getArticleContent());

        for (Element element : articles) {

            final Article article = new Article();
            article.setPublication(publication);
            article.setUrl(articleUrl);
            article.setTitle(getText(element, selector.getArticleTitle()));
            article.setSummary(removePrefix(getText(element, selector.getArticleSummary()), "Resumo"));
            article.setAuthor(getText(element, selector.getArticleAuthor()));
            article.setKeywords(removePrefix(getText(element, selector.getKeywords()), "Palavras-chave"));

            this.articleService.save(article);

            List<URLConnection> downloads = this.findDownloads(element, article, selector);
            if (downloads != null) {

                ArticleFileConnection articleFile = new ArticleFileConnection(downloads);
                VisitContext context = new ArticleFileContext(article, articleFile);
                this.visitor.visit(context);

            } else {

                String message = " PDF não encontrado.\nArtigo '"
                        + article.getTitle()
                        + "' URL '"
                        + articleUrl
                        + "'.\nPublicacão '"
                        + publication.getName()
                        + "'\nURL '"
                        + publication.getUrl()
                        + "'.";

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

    private List<URLConnection> findDownloads(Element element, Article article, OJSSelector selector) {

        List<URLConnection> connections = new ArrayList<>();
        String pdfUrl;

        // Search for PDF download
        List<Element> links = element.select(selector.getDownloadFile());
        try {

            for (Element link : links) {

                String href = link.attr("href");
                if (href.startsWith(article.getUrl())) {
                    pdfUrl = link.attr("href").replaceAll("article/view", "article/download");
                    URLConnection connection = new URL(pdfUrl).openConnection();
                    connection.addRequestProperty("User-Agent", "Mozilla/4.76");
                    connections.add(connection);
                }

            }

            return connections;

        } catch (IOException e) {
            return null;
        }

    }

}
