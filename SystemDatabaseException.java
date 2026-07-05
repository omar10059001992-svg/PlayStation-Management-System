
package gaming;

// Custom exception for handling database retrieval and connection errors

public class SystemDatabaseException extends Exception {
    public SystemDatabaseException(String message) {
        super(message);
    }
}
