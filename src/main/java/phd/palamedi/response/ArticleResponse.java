package phd.palamedi.response;

import org.springframework.beans.BeanUtils;
import phd.palamedi.model.Article;
import phd.palamedi.model.Publisher;
import phd.palamedi.model.Status;

/**
 * Created by marcos.salomao on 30/3/18.
 */
public class ArticleResponse {

    private Integer id;
    private String publisherName;
    private String url;
    private String fileName;
    private Status status;
    private String error;
    private Boolean selected;

    public ArticleResponse(Article article) {
        BeanUtils.copyProperties(article, this);
        this.setPublisherName(article.getPublication().getPublisher().getName());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
