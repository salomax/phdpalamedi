package phd.palamedi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phd.palamedi.model.Tag;
import phd.palamedi.repository.TagRepository;

import java.util.List;

/**
 * Created by marcos.salomao on 7/4/18.
 */
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public void save(Tag tag) {
        this.tagRepository.save(tag);
    }

    public void remove(Tag tag) {
        this.tagRepository.deleteByUrl(tag.getUrl());
    }

    public List<Tag> listByUrl(String url) {
        return  this.tagRepository.findByUrl(url);
    }

    public List<String> listTagByUrl(String url) {
        return  this.tagRepository.findTagByUrl(url);
    }

    public List<String> findAllTags() {
        return  this.tagRepository.findAllTags();
    }
}
