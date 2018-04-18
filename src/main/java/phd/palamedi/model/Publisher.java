package phd.palamedi.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by marcos.salomao on 14/12/17.
 */
@Entity
@Table(name="TB_PUBLISHER")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "publisher")
    private List<PublisherUrl> url;
    @OneToMany(mappedBy = "publisher")
    private List<PublisherFilter> filters;
    @OneToMany(mappedBy = "publisher")
    private List<Publication> publications;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PublisherUrl> getUrl() {
        return url;
    }

    public void setUrl(List<PublisherUrl> url) {
        this.url = url;
    }

    public List<PublisherFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<PublisherFilter> filters) {
        this.filters = filters;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }
}
