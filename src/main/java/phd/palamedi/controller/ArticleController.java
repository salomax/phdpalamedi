package phd.palamedi.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import phd.palamedi.exception.AcademicsException;
import phd.palamedi.model.Article;
import phd.palamedi.response.ArticleResponse;
import phd.palamedi.response.SearchResponse;
import phd.palamedi.service.ArticleService;

import java.util.*;

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

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView searchExportExcel(@RequestParam("search") String search,
                                                   @RequestParam(value = "filters", required = false) List<String> filters)
            throws AcademicsException {

        if (CollectionUtils.isEmpty(filters)) {
            filters = Lists.newArrayList("content", "title", "author", "summary", "keywords");
        }

        SearchResponse searchResponse = this.articleService.findByContent(search, filters);
        return new ModelAndView(new ArticleExcelView(searchResponse.getArticles()));
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public SearchResponse filter(@RequestParam("tags") String[] tags) {
        return this.articleService.findByTags(Arrays.asList(tags));
    }

    @RequestMapping(value = "/filter/excel", method = RequestMethod.GET)
    public ModelAndView filterExportExcel(@RequestParam("tags") String[] tags)
            throws AcademicsException {

        SearchResponse searchResponse = this.articleService.findByTags(Arrays.asList(tags));
        return new ModelAndView(new ArticleExcelView(searchResponse.getArticles()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Article get(@PathVariable Integer id) {
        return this.articleService.get(id);
    }

}
