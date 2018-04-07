package phd.palamedi.response;

import org.springframework.beans.BeanUtils;
import phd.palamedi.model.ArticleContent;
import phd.palamedi.model.Status;

/**
 * Created by marcos.salomao on 7/4/18.
 */
public class ArticleContentResponse {

    private Integer id;
    private String url;
    private Status status;

    public ArticleContentResponse(ArticleContent articleContent) {
        BeanUtils.copyProperties(articleContent, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
