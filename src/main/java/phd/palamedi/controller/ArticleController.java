package phd.palamedi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phd.palamedi.model.Article;
import phd.palamedi.response.SearchResponse;
import phd.palamedi.service.ArticleService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public SearchResponse search(@RequestParam("search") String search) {
        return this.articleService.findByContent(search);
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public SearchResponse search(@RequestParam("tags") String[] tags) {
        return this.articleService.findByTags(Arrays.asList(tags));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Article get(@PathVariable Integer id) {
        return this.articleService.get(id);
    }

}
