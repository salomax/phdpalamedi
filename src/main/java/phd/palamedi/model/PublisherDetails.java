package phd.palamedi.model;

/**
 * Created by marcos.salomao on 25/3/18.
 */
public class PublisherDetails {

    private int totalArticles;
    private int totalArticlesSuccess;
    private int totalArticlesError;

    public int getTotalArticles() {
        return totalArticles;
    }

    public void setTotalArticles(int totalArticles) {
        this.totalArticles = totalArticles;
    }

    public int getTotalArticlesSuccess() {
        return totalArticlesSuccess;
    }

    public void setTotalArticlesSuccess(int totalArticlesSuccess) {
        this.totalArticlesSuccess = totalArticlesSuccess;
    }

    public int getTotalArticlesError() {
        return totalArticlesError;
    }

    public void setTotalArticlesError(int totalArticlesError) {
        this.totalArticlesError = totalArticlesError;
    }
}
