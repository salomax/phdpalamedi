package phd.palamedi.model;

/**
 * Created by marcos.salomao on 25/3/18.
 */
public class PublisherDetails {

    private int totalArticles;
    private int totalErrors;

    public int getTotalArticles() {
        return totalArticles;
    }

    public void setTotalArticles(int totalArticles) {
        this.totalArticles = totalArticles;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
    }
}
