package util;

import exception.DuplicateRecordException;
import exception.RecordNotFoundException;

import java.sql.SQLException;

/** Turns exceptions into short, friendly messages for the console. */
public final class ErrorHandler {

    private ErrorHandler() {
    }

    public static void handle(Exception e) {
        if (e instanceof SQLException) {
            System.out.println("Database error: " + e.getMessage());
        } else if (e instanceof RecordNotFoundException
                || e instanceof DuplicateRecordException
                || e instanceof IllegalArgumentException) {
            System.out.println("Error: " + e.getMessage());
        } else {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
