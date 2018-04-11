package phd.palamedi.crawler.impl;

import org.springframework.stereotype.Component;
import phd.palamedi.crawler.OJSSelector;

/**
 * Created by marcos.salomao on 11/4/18.
 */
@Component
public class CompolisticaOJSSelector implements OJSSelector {

    @Override
    public String getIssuesArchive() {
        return "ul.issues_archive";
    }

    @Override
    public String getIssues() {
        return "div.obj_issue_summary";
    }

    @Override
    public String getPublicationContent() {
        return "div.page_issue";
    }

    @Override
    public String getPublicationTitle() {
        return "li.current h1";
    }

    @Override
    public String getArticles() {
        return "div.obj_article_summary";
    }

    @Override
    public String getArticleUrl() {
        return "div.title a";
    }

    @Override
    public String getArticleContent() {
        return "div.page_article";
    }

    @Override
    public String getArticleTitle() {
        return "h1.page_title";
    }

    @Override
    public String getArticleSummary() {
        return "div.abstract p";
    }

    @Override
    public String getArticleAuthor() {
        return "ul.authors li";
    }

    @Override
    public String getKeywords() {
        return "XXXX";
    }

    @Override
    public String getDownloadFile() {
        return "a.pdf";
    }
}
