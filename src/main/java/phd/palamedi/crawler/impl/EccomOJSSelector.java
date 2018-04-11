package phd.palamedi.crawler.impl;

import org.springframework.stereotype.Component;
import phd.palamedi.crawler.OJSSelector;

/**
 * Created by marcos.salomao on 11/4/18.
 */
@Component
public class EccomOJSSelector implements OJSSelector {

    @Override
    public String getIssuesArchive() {
        return "div[id=main]";
    }

    @Override
    public String getIssues() {
        return "h4";
    }

    @Override
    public String getPublicationContent() {
        return "div[id=main]";
    }

    @Override
    public String getPublicationTitle() {
        return "h2";
    }

    @Override
    public String getArticles() {
        return "table.tocArticle";
    }

    @Override
    public String getArticleUrl() {
        return ".tocGalleys a";
    }

    @Override
    public String getArticleContent() {
        return "div[id=content]";
    }

    @Override
    public String getArticleTitle() {
        return "h3";
    }

    @Override
    public String getArticleSummary() {
        return "div p.MsoNormal";
    }

    @Override
    public String getArticleAuthor() {
        return "div em";
    }

    @Override
    public String getKeywords() {
        return "XXXX";
    }

    @Override
    public String getDownloadFile() {
        return "a.file";
    }
}
