package phd.palamedi.model;

import java.net.URLConnection;
import java.util.List;

/**
 * Created by marcos.salomao on 15/12/17.
 */
public class ArticleFileConnection {

    private List<URLConnection> urlConnections;

    public ArticleFileConnection(List<URLConnection> urlConnections) {
        this.urlConnections = urlConnections;
    }

    public List<URLConnection> getUrlConnections() {
        return urlConnections;
    }

}
