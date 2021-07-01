package com.catalog.exception;


import com.catalog.exception.errors.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;
    private String message;

    /**
     * Default Constructor.
     *
     * @param errorCode the errorCode
     * @param message   the error message.
     */
    public ApplicationException(ErrorCode errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Default Constructor.
     *
     * @param errorCode the errorCode
     */
    public ApplicationException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
        this.message = errorCode.getDescription();
    }
}

