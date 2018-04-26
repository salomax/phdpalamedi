package phd.palamedi.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import phd.palamedi.exception.AcademicsException;
import phd.palamedi.model.Article;
import phd.palamedi.response.SearchResponse;
import phd.palamedi.service.ArticleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public SearchResponse search(@RequestParam("search") String search,
                                 @RequestParam(value = "filters", required = false) List<String> filters,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page)
            throws AcademicsException {

        if (CollectionUtils.isEmpty(filters)) {
            filters = Lists.newArrayList("content", "title", "author", "summary", "keywords");
        }

        return this.articleService.findByContent(search, page, filters);
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
