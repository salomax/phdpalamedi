package phd.palamedi.response;

import org.springframework.beans.BeanUtils;
import phd.palamedi.index.model.ArticleIndex;
import phd.palamedi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcos.salomao on 30/3/18.
 */
public class ArticleResponse {

    private Integer id;
    private PublicationResponse publication;
    private String url;
    private String title;
    private String author;
    private String summary;
    private String keywords;
    private List<String> tags;
    private List<ArticleContentResponse> articleContents = new ArrayList<>();

    public ArticleResponse(Article article) {
        BeanUtils.copyProperties(article, this);
        this.setPublication(new PublicationResponse(article.getPublication()));
    }

    public ArticleResponse(ArticleIndex article) {
        BeanUtils.copyProperties(article, this);
        this.setId(Integer.valueOf(article.getId()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PublicationResponse getPublication() {
        return publication;
    }

    public void setPublication(PublicationResponse publication) {
        this.publication = publication;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ArticleContentResponse> getArticleContents() {
        return articleContents;
    }

    public void setArticleContents(List<ArticleContentResponse> articleContents) {
        this.articleContents = articleContents;
    }
}
