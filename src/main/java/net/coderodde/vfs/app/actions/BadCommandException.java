package net.coderodde.vfs.app.actions;

/**
 * Thrown on invalid commands.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 5, 2017)
 */
public class BadCommandException extends RuntimeException {

    public BadCommandException(String message) {
        super(message);
    }
}
