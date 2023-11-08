package ru.garrowd.admissionservice.utils.exceptions;

import ru.garrowd.admissionservice.utils.exceptions.generics.GenericException;

public class ApplicationStatusException
        extends GenericException {
    public ApplicationStatusException(String message) {
        super(message);
    }
}
