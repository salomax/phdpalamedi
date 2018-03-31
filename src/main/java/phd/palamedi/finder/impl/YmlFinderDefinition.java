package phd.palamedi.finder.impl;

import phd.palamedi.finder.FinderDefinition;
import phd.palamedi.exception.AcademicsException;
import phd.palamedi.model.Finder;
import phd.palamedi.model.Publisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by marcos.salomao on 14/12/17.
 */
@Component
public class YmlFinderDefinition implements FinderDefinition {

    @Override
    public List<Publisher> getPublishers() throws AcademicsException {

        String fileName = "/finder-definition.yml";

        try {

            InputStream inputStream = this.getClass().getResourceAsStream("/finder-definition.yml");

            if (inputStream == null) {
                throw new FileNotFoundException("File " + fileName + " not found");
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            return mapper.readValue(inputStream, Finder.class).getPublishers();

        } catch (Exception e) {
            throw new AcademicsException("Finder definition file can not be read", e);
        }

    }

}
