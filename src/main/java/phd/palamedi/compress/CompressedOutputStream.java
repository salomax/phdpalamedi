package phd.palamedi.compress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by marcos.salomao on 25/3/18.
 */
public class CompressedOutputStream extends GZIPOutputStream {

    public CompressedOutputStream(OutputStream outputStream) throws IOException {
        super(outputStream);
//        def.setLevel(Deflater.BEST_COMPRESSION);
    }

}