package phd.palamedi.response;

import org.springframework.beans.BeanUtils;
import phd.palamedi.model.ArticleContent;

/**
 * Created by marcos.salomao on 7/4/18.
 */
public class ContentResponse {

    private String content;

    public ContentResponse(ArticleContent articleContent) {
        BeanUtils.copyProperties(articleContent, this);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
