package phd.palamedi.response;

import org.springframework.beans.BeanUtils;
import phd.palamedi.index.model.PublicationIndex;
import phd.palamedi.model.Publication;

/**
 * Created by marcos.salomao on 7/4/18.
 */
public class PublicationResponse {

    private String publisher;
    private String url;
    private String name;
    private Integer year;

    public PublicationResponse(Publication publication) {
        BeanUtils.copyProperties(publication, this);
        this.setPublisher(publication.getPublisher().getName());
    }

    public PublicationResponse(PublicationIndex publication) {
        BeanUtils.copyProperties(publication, this);
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
