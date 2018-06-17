package phd.palamedi.index.model;

/**
 * Created by marcos.salomao on 17/6/18.
 */
public class PublicationIndex {

    private String publisher;
    private String url;
    private String name;
    private Integer year;

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
