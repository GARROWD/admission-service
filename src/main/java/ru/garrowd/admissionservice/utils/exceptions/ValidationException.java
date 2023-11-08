package ru.garrowd.admissionservice.utils.exceptions;

import ru.garrowd.admissionservice.utils.exceptions.generics.GenericExceptionWithDetails;
import ru.garrowd.admissionservice.utils.exceptions.messages.GenericMessage;
import java.util.Set;

public class ValidationException
        extends GenericExceptionWithDetails {
    public ValidationException(Set<GenericMessage> messages) {
        super(messages);
    }
}
