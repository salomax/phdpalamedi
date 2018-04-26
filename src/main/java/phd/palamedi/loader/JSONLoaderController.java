package phd.palamedi.loader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Created by marcos.salomao on 17/4/18.
 */
@RestController
@RequestMapping("/jsonloader")
public class JSONLoaderController {

    private static final Logger LOGGER = Logger.getLogger(JSONLoaderController.class.toString());

    @Autowired
    private ArticleImporter articleImporter;

    @RequestMapping("{file}")
    public void get(@PathVariable("file") String fileName) throws IOException {

        InputStream inputStream = new ClassPathResource(fileName).getInputStream();

        if (inputStream != null) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            ArticleLoader[] articleLoaders = mapper.readValue(inputStream, ArticleLoader[].class);

            for (ArticleLoader articleLoader : articleLoaders) {
                articleImporter.load(articleLoader);
            }

            LOGGER.info("Total de artigos carregados " + articleLoaders.length);

        }

    }

}
