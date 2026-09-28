package exception;

/** Thrown when a student, course or enrollment cannot be found. */
public class RecordNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(String message) {
        super(message);
    }
}
