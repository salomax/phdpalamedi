package phd.palamedi.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcos.salomao on 30/3/18.
 */
public class SearchResponse {

    private Long total;
    private String query;
    private List<ArticleResponse> articles;
    List<String> terms = new ArrayList<>();
    List<String> tags = new ArrayList<>();

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<ArticleResponse> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleResponse> articles) {
        this.articles = articles;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
