package edu.hm.hafner.java.util;

import org.springframework.core.NestedRuntimeException;

/**
 * Technical exception which typically cannot be handled by code.
 *
 * @author Ullrich Hafner
 */
public class TechnicalException extends NestedRuntimeException {
    public TechnicalException(final String msg) {
        super(msg);
    }

    public TechnicalException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
