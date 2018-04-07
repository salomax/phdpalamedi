package phd.palamedi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phd.palamedi.model.Article;
import phd.palamedi.model.ArticleContent;
import phd.palamedi.response.ContentResponse;
import phd.palamedi.response.SearchResponse;
import phd.palamedi.service.ArticleContentService;
import phd.palamedi.service.ArticleService;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@RestController
@RequestMapping("/articlecontent")
public class ArticleContentController {

    @Autowired
    private ArticleContentService articleContentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ContentResponse get(@PathVariable Integer id) {
        return new ContentResponse(this.articleContentService.get(id));
    }

}
