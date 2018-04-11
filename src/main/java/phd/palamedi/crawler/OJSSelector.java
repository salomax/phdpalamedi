package phd.palamedi.crawler;

/**
 * Created by marcos.salomao on 11/4/18.
 */
public interface OJSSelector {

    String getIssuesArchive();

    String getIssues();

    String getPublicationContent();

    String getPublicationTitle();

    String getArticles();

    String getArticleUrl();

    String getArticleContent();

    String getArticleTitle();

    String getArticleSummary();

    String getArticleAuthor();

    String getKeywords();

    String getDownloadFile();
}
