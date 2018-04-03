package phd.palamedi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phd.palamedi.model.Publication;
import phd.palamedi.model.Publisher;

/**
 * Created by marcos.salomao on 24/3/18.
 */
public interface PublicationRepository extends CrudRepository<Publication, Integer> {

    @Transactional
    void deleteByPublisher(Publisher publisher);
}
