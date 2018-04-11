package phd.palamedi.crawler.impl;

import org.springframework.stereotype.Component;
import phd.palamedi.crawler.OJSSelector;

/**
 * Created by marcos.salomao on 11/4/18.
 */
@Component
public class DefaultOJSSelector implements OJSSelector {

    @Override
    public String getIssuesArchive() {
        return "div[id=issues]";
    }

    @Override
    public String getIssues() {
        return "div[id^=issue]";
    }

    @Override
    public String getPublicationContent() {
        return "body";
    }

    @Override
    public String getPublicationTitle() {
        return "h2,h3";
    }

    @Override
    public String getArticles() {
        return "table.tocArticle";
    }

    @Override
    public String getArticleUrl() {
        return ".tocTitle a";
    }

    @Override
    public String getArticleContent() {
        return "div[id=content]";
    }

    @Override
    public String getArticleTitle() {
        return "div[id=articleTitle]";
    }

    @Override
    public String getArticleSummary() {
        return "div[id=articleAbstract]";
    }

    @Override
    public String getArticleAuthor() {
        return "div[id=authorString]";
    }

    @Override
    public String getKeywords() {
        return "div[id=articleSubject]";
    }

    @Override
    public String getDownloadFile() {
        return "a.file";
    }
}
