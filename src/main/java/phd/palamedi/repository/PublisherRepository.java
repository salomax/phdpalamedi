package phd.palamedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phd.palamedi.model.Publisher;

/**
 * Created by marcos.salomao on 24/3/18.
 */
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
}
