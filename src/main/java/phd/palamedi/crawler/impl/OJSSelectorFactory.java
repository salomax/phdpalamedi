package phd.palamedi.crawler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import phd.palamedi.crawler.OJSSelector;
import phd.palamedi.model.Publisher;

/**
 * Created by marcos.salomao on 11/4/18.
 */
@Component
public class OJSSelectorFactory {

    @Autowired
    private DefaultOJSSelector defaultOJSSelector;

    @Autowired
    private CompolisticaOJSSelector compolisticaOJSSelector;

    @Autowired
    private EccomOJSSelector eccomOJSSelector;

    public OJSSelector get(Publisher publisher) {

        if (publisher.getName().equalsIgnoreCase("Compolitica")) {
            return compolisticaOJSSelector;
        }

        if (publisher.getName().equalsIgnoreCase("ECCOm - Educação, Cultura e Comunicação")) {
            return this.eccomOJSSelector;
        }

        return defaultOJSSelector;

    }

}
