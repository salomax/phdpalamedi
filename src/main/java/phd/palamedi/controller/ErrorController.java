package phd.palamedi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phd.palamedi.model.Article;
import phd.palamedi.model.Error;
import phd.palamedi.response.ErrorResponse;
import phd.palamedi.response.SearchResponse;
import phd.palamedi.service.ArticleService;
import phd.palamedi.service.ErrorService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@RestController
@RequestMapping("/error")
public class ErrorController {

    @Autowired
    private ErrorService errorService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ErrorResponse> search(@RequestParam(value = "publisher_id", required = false) Integer publisherId) {

        List<Error> errors;
        if (publisherId == null) {
            errors = this.errorService.search();
        } else {
            errors = this.errorService.search(publisherId);
        }

        return errors.stream().map(error -> new ErrorResponse(error)).collect(Collectors.toList());
    }

}
