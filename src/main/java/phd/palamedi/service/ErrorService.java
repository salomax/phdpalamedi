package phd.palamedi.service;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phd.palamedi.model.Error;
import phd.palamedi.model.Publisher;
import phd.palamedi.repository.ErrorRepository;

import java.util.List;

/**
 * Created by marcos.salomao on 2/4/18.
 */
@Service
public class ErrorService {

    @Autowired
    private ErrorRepository errorRepository;

    public void save(Error error) {
        this.errorRepository.save(error);
    }

    public void save(Publisher publisher, String message) {
        this.save(publisher, message, null);
    }

    public void save(Publisher publisher, Exception e) {
        this.save(publisher, e.getMessage(), e);
    }

    public void save(Publisher publisher, String message, Exception e) {
        Error error = new Error();
        error.setPublisher(publisher);
        error.setMessage(message);
        if (e != null) {
            error.setException(ExceptionUtils.getStackTrace(e));
        }
        this.save(error);

    }

    public void deleteByPublisher(Publisher publisher) {
        this.errorRepository.deleteByPublisher(publisher);
    }

    public List<Error> search() {
        return this.errorRepository.findAll();
    }

    public List<Error> search(Integer publisherId) {
        Publisher publisher = new Publisher();
        publisher.setId(publisherId);
        return this.errorRepository.findByPublisher(publisher);
    }
}
