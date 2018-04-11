package phd.palamedi.response;

import phd.palamedi.model.Publisher;
import phd.palamedi.model.PublisherFilter;
import phd.palamedi.model.PublisherUrl;

import java.util.List;

/**
 * Created by marcos.salomao on 7/4/18.
 */
public class PublisherResponse {

    private Integer id;
    private String name;
    private List<PublisherFilter> filters;
    private List<PublisherUrl> url;

    public PublisherResponse(Publisher publisher) {
        this.setId(publisher.getId());
        this.setName(publisher.getName());
        this.setUrl(publisher.getUrl());
        this.setFilters(publisher.getFilters());
    }

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

    public List<PublisherFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<PublisherFilter> filters) {
        this.filters = filters;
    }

    public List<PublisherUrl> getUrl() {
        return url;
    }

    public void setUrl(List<PublisherUrl> url) {
        this.url = url;
    }
}
