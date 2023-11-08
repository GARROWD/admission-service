package ru.garrowd.admissionservice.utils.exceptions;

import ru.garrowd.admissionservice.utils.exceptions.generics.GenericException;

public class NotFoundException
        extends GenericException {
    public NotFoundException(String message){
        super(message);
    }
}