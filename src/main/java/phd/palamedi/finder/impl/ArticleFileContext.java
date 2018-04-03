package phd.palamedi.finder.impl;

import phd.palamedi.finder.VisitContext;
import phd.palamedi.model.Article;
import phd.palamedi.model.ArticleFileConnection;
import phd.palamedi.model.Publisher;

/**
 * Created by marcos.salomao on 14/12/17.
 */
public class ArticleFileContext implements VisitContext<ArticleFileConnection> {

    private ArticleFileConnection articleFile;
    private Article article;

    public ArticleFileContext(Article article, ArticleFileConnection articleFile) {
        this.articleFile = articleFile;
        this.article = article;
    }

    @Override
    public ArticleFileConnection get() {
        return this.articleFile;
    }

    public Article getArticle() {
        return article;
    }

}
