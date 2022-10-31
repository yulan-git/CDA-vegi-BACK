package com.vegi.vegilabback.exception.exceptions;

import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String msg)
    {
        super(msg);
    }

    public ResourceNotFoundException(String msg, Long id, Throwable cause)
    {
        super(String.format(msg, id), cause);
    }
}
