package phd.palamedi.response;

import org.springframework.beans.BeanUtils;
import phd.palamedi.model.Error;

import java.util.Date;

/**
 * Created by marcos.salomao on 5/4/18.
 */
public class ErrorResponse {

    private Integer id;
    private String message;
    private String exception;
    private Date created;
    private String publisherName;

    public ErrorResponse(Error error) {
        BeanUtils.copyProperties(error, this);
        this.setPublisherName(error.getPublisher().getName());
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
