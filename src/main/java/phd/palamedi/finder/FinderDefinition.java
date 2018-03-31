package phd.palamedi.finder;

import phd.palamedi.exception.AcademicsException;
import phd.palamedi.model.Publisher;

import java.util.List;

/**
 * Created by marcos.salomao on 14/12/17.
 */
public interface FinderDefinition {

    List<Publisher> getPublishers() throws AcademicsException;

}
