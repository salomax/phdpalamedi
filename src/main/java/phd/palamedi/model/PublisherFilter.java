package phd.palamedi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by marcos.salomao on 24/3/18.
 */
@Entity
@Table(name = "TB_PUBLISHER_Filter")
public class PublisherFilter {

    @Id
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    @JsonIgnore
    private Publisher publisher;
    private String url;

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
