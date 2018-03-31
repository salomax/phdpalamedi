package phd.palamedi.finder;

import phd.palamedi.exception.AcademicsException;
import phd.palamedi.model.Summary;

import java.util.Set;

/**
 * Created by marcos.salomao on 15/12/17.
 */
public interface SummaryGenerator {

    void save(Set<Summary> summaries) throws AcademicsException;

}
