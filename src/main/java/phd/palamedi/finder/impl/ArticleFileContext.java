package phd.palamedi.finder.impl;

import phd.palamedi.finder.VisitContext;
import phd.palamedi.model.ArticleFileConnection;
import phd.palamedi.model.Publisher;

/**
 * Created by marcos.salomao on 14/12/17.
 */
public class ArticleFileContext implements VisitContext<ArticleFileConnection> {

    private ArticleFileConnection articleFile;
    private Publisher publisher;

    public ArticleFileContext(Publisher publisher, ArticleFileConnection articleFile) {
        this.articleFile = articleFile;
        this.publisher = publisher;
    }

    @Override
    public ArticleFileConnection get() {
        return this.articleFile;
    }

    public Publisher getPublisher() {
        return publisher;
    }

}
