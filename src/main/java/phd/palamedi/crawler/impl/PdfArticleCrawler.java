package phd.palamedi.crawler.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import phd.palamedi.crawler.ArticleCrawler;
import phd.palamedi.finder.ArticleVisitor;
import phd.palamedi.finder.VisitContext;
import phd.palamedi.finder.impl.ArticleFileContext;
import phd.palamedi.model.ArticleFileConnection;
import phd.palamedi.model.Publisher;
import phd.palamedi.model.PublisherFilter;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by marcos.salomao on 13/12/17.
 */
@Component
public class PdfArticleCrawler implements ArticleCrawler {

    private static final Logger LOGGER = Logger.getLogger(PdfArticleCrawler.class.toString());

    private Set<String> visitedList = new HashSet<>();
    private Set<String> links = new HashSet<>();

    @Autowired
    private ArticleVisitor visitor;

    @Autowired
    private TaskExecutor executor;

    @Override
    public void execute(Publisher publisher) {

        this.links.clear();

        this.visitedList.addAll(publisher.getFilters().stream()
                .map(PublisherFilter::getUrl).collect(Collectors.toList()));

        publisher.getUrl().stream().forEach(url ->
                this.executor.execute(() -> getPageLinks(publisher, url.getUrl()))
        );

    }

    protected void getPageLinks(Publisher publisher, String url) {

        // Check if you have already crawled the URLs
        URLConnection urlConnection;
        try {

            if (!links.contains(url)) {

                // add it to the index
                links.add(url);

                // Open connection
                urlConnection = new URL(url).openConnection();

                if (urlConnection == null) {

                    LOGGER.severe("Cannot open connection to " + url);

                } else if (isPdf(urlConnection)) {

                    ArticleFileConnection articleFile = new ArticleFileConnection(urlConnection);
                    VisitContext context = new ArticleFileContext(publisher, articleFile);
                    this.visitor.visit(context);

                } else {

                    // Parse the HTML to extract links to other URLs
                    InputStream input = urlConnection.getInputStream();
                    Document document = Jsoup.parse(input, "UTF-8", url);
                    Elements linksOnPage = document.select("a");

                    // For each extracted URL recursively
                    for (Element page : linksOnPage) {
                        String aValue = page.attr("abs:href");

                        final String link;
                        if (StringUtils.isEmpty(aValue)) {

                            // extract link from js code
                            link = extractUrlFromScript(page);

                        } else {
                            link = aValue;
                        }

                        if (isVisited(link)) {
                            this.executor.execute(() -> getPageLinks(publisher, link));
                        }

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe(e.getMessage());
        }

    }

    private String extractUrlFromScript(Element page) {
        String link;
        String onClick = page.attr("onclick");
        Pattern pattern = Pattern.compile("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?");
        Matcher matcher = pattern.matcher(onClick);
        if (matcher.find()) {
            link = onClick.substring(matcher.start(), matcher.end());
        } else {
            link = null;
        }
        return link;
    }

    private boolean isVisited(String link) {
        return !StringUtils.isEmpty(link) && this.visitedList.stream().filter(l -> link.startsWith(l)).count() > 0;
    }

    private boolean isPdf(URLConnection urlConnection) {

        if (StringUtils.isEmpty(urlConnection.getContentType())) {
            return true;
        }

        return urlConnection.getContentType().toLowerCase().contains("pdf") ||
                urlConnection.getContentType().toLowerCase().contains("download");
    }

}
