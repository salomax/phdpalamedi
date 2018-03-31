package phd.palamedi.model;

import java.net.URLConnection;

/**
 * Created by marcos.salomao on 15/12/17.
 */
public class ArticleFileConnection {

    private URLConnection urlConnection;

    public ArticleFileConnection(URLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

    public URLConnection getUrlConnection() {
        return urlConnection;
    }
}
