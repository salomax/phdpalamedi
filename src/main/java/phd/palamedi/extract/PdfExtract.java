package phd.palamedi.extract;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import phd.palamedi.exception.AcademicsException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by marcos.salomao on 25/3/18.
 */
@Component
public class PdfExtract {

    public String extract(InputStream inputStream) throws AcademicsException {

        try {
            ContentHandler contenthandler = new BodyContentHandler(200000);
            Metadata metadata = new Metadata();
            Parser parser = new AutoDetectParser(new DefaultDetector());
            parser.parse(inputStream, contenthandler, metadata, new ParseContext());
            return contenthandler.toString();
        } catch (Exception e) {

            throw new AcademicsException("Não foi possível realizar parse do arquivo: " + e.getMessage(), e);

        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new AcademicsException("Não foi possível realizar parse do arquivo. " +
                            "Erro ao finalizar InputStream", e);
                }
            }

        }

    }

}
