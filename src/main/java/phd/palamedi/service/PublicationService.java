package phd.palamedi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phd.palamedi.model.Publication;
import phd.palamedi.model.Publisher;
import phd.palamedi.repository.PublicationRepository;

import java.util.logging.Logger;

/**
 * Created by marcos.salomao on 2/4/18.
 */
@Service
public class PublicationService {

    private static final Logger LOGGER = Logger.getLogger(ArticleService.class.toString());

    @Autowired
    private PublicationRepository publicationRepository;

    @Transactional
    public Publication save(Publication publication) {
        LOGGER.info("Saving publication " + publication.getUrl());
        return this.publicationRepository.save(publication);
    }

    public Publication getByPublisherAndName(Publisher publisher, String publicationName) {
        return this.publicationRepository.getByPublisherAndName(publisher, publicationName);
    }
}
