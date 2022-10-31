package com.vegi.vegilabback.exception.exceptions;

public class IngredientException extends RuntimeException{
    public IngredientException() {}

    public IngredientException(String msg)
    {
        super(msg);
    }

    public IngredientException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
