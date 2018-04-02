package phd.palamedi.extract;

import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.metadata.Metadata;
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
            ContentHandler contenthandler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputStream, contenthandler, metadata, new ParseContext());
            return contenthandler.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AcademicsException("PDF not extracted", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new AcademicsException("PDF not extracted. Input stream not closed", e);
                }
            }
        }

    }

}
