package phd.palamedi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phd.palamedi.model.Tag;
import phd.palamedi.response.ContentResponse;
import phd.palamedi.service.ArticleContentService;
import phd.palamedi.service.TagService;

import java.util.List;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestBody  Tag tag) {
        this.tagService.save(tag);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void remove(@RequestBody Tag tag) {
        this.tagService.remove(tag);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Tag> list(@RequestBody Tag tag) {
        return this.tagService.listByUrl(tag.getUrl());
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<String> listAll() {
        return this.tagService.findAllTags();
    }

}
