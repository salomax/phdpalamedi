package phd.palamedi.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import phd.palamedi.exception.AcademicsException;
import phd.palamedi.index.model.ArticleIndex;
import phd.palamedi.response.ArticleResponse;
import phd.palamedi.response.SearchResponse;
import phd.palamedi.service.ArticleIndexService;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by marcos.salomao on 16/6/18.
 */
@RestController
@RequestMapping("/index/article")
public class ArticleIndexController {

    @Autowired
    private ArticleIndexService articleIndexService;

    @RequestMapping(method = RequestMethod.POST)
    public void indexAll() {
        this.articleIndexService.indexAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public SearchResponse find(@RequestParam("search") String search,
                                 @RequestParam(value = "filters", required = false) List<String> filters,
                                 @RequestParam(value = "page", required = false, defaultValue = "0") Integer page)
            throws AcademicsException {

        if (CollectionUtils.isEmpty(filters)) {
            filters = Lists.newArrayList("content", "title", "author", "summary", "keywords");
        }

        return this.articleIndexService.find(search, page, filters);
    }

}
