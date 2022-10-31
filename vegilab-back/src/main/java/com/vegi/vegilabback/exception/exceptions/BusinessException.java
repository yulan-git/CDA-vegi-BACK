package com.vegi.vegilabback.exception.exceptions;

public class BusinessException extends RuntimeException{

    public BusinessException() {}

    public BusinessException(String msg)
    {
        super(msg);
    }

    public BusinessException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
