package pl.blali733.counters.exceptions;

/**
 * Transaction failed exception.
 * @author blali733
 * @version 1.0
 * @since 0.4 app / 1.0 package
 */
public class TransactionException extends Exception {
    public TransactionException() { super(); }
    public TransactionException(String message) { super(message); }
    public TransactionException(String message, Throwable cause) { super(message, cause); }
    public TransactionException(Throwable cause) { super(cause); }
}
