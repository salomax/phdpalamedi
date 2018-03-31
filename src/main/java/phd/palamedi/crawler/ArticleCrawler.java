package phd.palamedi.crawler;

import phd.palamedi.model.Publisher;
import phd.palamedi.model.Summary;
import phd.palamedi.finder.ArticleVisitor;

import java.util.Set;

/**
 * Created by marcos.salomao on 14/12/17.
 */
public interface ArticleCrawler {

    void execute(Publisher name);

}
