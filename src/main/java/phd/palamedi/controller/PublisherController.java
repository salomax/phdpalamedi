package phd.palamedi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import phd.palamedi.model.Publisher;
import phd.palamedi.model.PublisherDetails;
import phd.palamedi.response.PublisherResponse;
import phd.palamedi.service.PublisherService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marcos.salomao on 24/3/18.
 */
@RestController
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @RequestMapping(method = RequestMethod.GET)
    public List<PublisherResponse> get() {
        return this.publisherService.findAll().stream()
                .map(publisher -> new PublisherResponse(publisher)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public void post(@PathVariable Integer id) {

        this.publisherService.loadPublisher(id);

    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public PublisherDetails details(@PathVariable Integer id) {

        return this.publisherService.getDetails(id);

    }

}
