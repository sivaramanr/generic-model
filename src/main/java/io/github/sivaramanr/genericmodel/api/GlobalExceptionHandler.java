package io.github.sivaramanr.genericmodel.api;

import io.github.sivaramanr.common.types.GenericType;
import io.jsonwebtoken.io.DecodingException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final class ErrorKeys {
        static final String TIMESTAMP = "timestamp";
        static final String STATUS = "status";
        static final String ERROR = "error";
        static final String MESSAGE = "message";
        static final String FIELD_ERRORS = "fieldErrors";
    }

    private static final class ErrorMessages {
        static final String VALIDATION_FAILED = "Validation Failed";
        static final String BAD_REQUEST = "Bad Request";
        static final String ENUM_INVALID = "Invalid value for enum";
        static final String GENERIC_TYPE_NOT_SUPPORTED = "genericType is not supported. Allowed values: ";
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, Object> errorResponse = Map.of(
                ErrorKeys.TIMESTAMP, LocalDateTime.now(),
                ErrorKeys.STATUS, HttpStatus.BAD_REQUEST.value(),
                ErrorKeys.ERROR, ErrorMessages.VALIDATION_FAILED,
                ErrorKeys.FIELD_ERRORS, ex.getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                DefaultMessageSourceResolvable::getDefaultMessage
                        ))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Map<String, Object>> handleEnumErrors(ServerWebInputException ex) {
        if (ex.getCause() instanceof DecodingException &&
                ex.getCause().getCause() instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    ErrorKeys.TIMESTAMP, LocalDateTime.now(),
                    ErrorKeys.STATUS, HttpStatus.BAD_REQUEST.value(),
                    ErrorKeys.ERROR, ErrorMessages.ENUM_INVALID,
                    ErrorKeys.MESSAGE, ErrorMessages.GENERIC_TYPE_NOT_SUPPORTED +
                            String.join(", ", getGenericTypeValues())
            ));
        }
        // fallback for other input exceptions
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                ErrorKeys.TIMESTAMP, LocalDateTime.now(),
                ErrorKeys.STATUS, HttpStatus.BAD_REQUEST.value(),
                ErrorKeys.ERROR, ErrorMessages.BAD_REQUEST,
                ErrorKeys.MESSAGE, ex.getReason()
        ));
    }

    private String[] getGenericTypeValues() {
        return java.util.Arrays.stream(GenericType.values())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(Exception ex) {
        Map<String, Object> errorResponse = Map.of(
                ErrorKeys.TIMESTAMP, LocalDateTime.now(),
                ErrorKeys.STATUS, HttpStatus.BAD_REQUEST.value(),
                ErrorKeys.ERROR, ErrorMessages.VALIDATION_FAILED
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
