package ru.garrowd.admissionservice.utils.exceptions;

import ru.garrowd.admissionservice.utils.exceptions.generics.GenericException;

public class UnexpectedException extends GenericException {
    public UnexpectedException(String message){
        super(message);
    }
}
