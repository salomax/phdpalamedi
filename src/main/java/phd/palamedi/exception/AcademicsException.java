package phd.palamedi.exception;

/**
 * Created by marcos.salomao on 13/12/17.
 */
public class AcademicsException extends Exception {
    public AcademicsException(String msg, Exception e) {
        super(msg, e);
    }
}
