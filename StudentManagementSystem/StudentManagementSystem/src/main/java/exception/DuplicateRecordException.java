package exception;

/** Thrown when we try to save a record that already exists (e.g. same email or course code). */
public class DuplicateRecordException extends Exception {

    private static final long serialVersionUID = 1L;

    public DuplicateRecordException(String message) {
        super(message);
    }
}
