package com.vegi.vegilabback.exception.exceptions;

public class TechnicalException extends RuntimeException{

    public TechnicalException() {}

    public TechnicalException(String msg)
    {
        super(msg);
    }

    public TechnicalException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
