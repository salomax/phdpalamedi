package phd.palamedi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phd.palamedi.model.ArticleContent;
import phd.palamedi.repository.ArticleContentRepository;

/**
 * Created by marcos.salomao on 4/4/18.
 */
@Service
public class ArticleContentService {

    @Autowired
    private ArticleContentRepository articleContentRepository;

    public void save(ArticleContent articleContent) {
        this.articleContentRepository.save(articleContent);
    }

}
