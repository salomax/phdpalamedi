package phd.palamedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import phd.palamedi.model.Article;
import phd.palamedi.model.Error;
import phd.palamedi.model.Publisher;

import java.util.List;

/**
 * Created by marcos.salomao on 24/3/18.
 */
public interface ErrorRepository extends JpaRepository<Error, Integer> {

    @Transactional
    void deleteByPublisher(Publisher publisher);

    List<Error> findByPublisher(Publisher publisher);

    @Query("SELECT COUNT(1) FROM Error e WHERE e.publisher = ?1")
    Integer countByPublisher(Publisher publisher);

}
