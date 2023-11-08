package ru.garrowd.admissionservice.utils.exceptions.generics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.garrowd.admissionservice.utils.exceptions.messages.GenericMessage;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericExceptionWithDetails
        extends RuntimeException{
    private Set<GenericMessage> genericMessageWithDetails;
}
