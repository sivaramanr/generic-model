package io.github.sivaramanr.genericmodel.api;

import io.github.sivaramanr.common.types.GenericType;
import io.jsonwebtoken.io.DecodingException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
                ErrorKeys.ERROR, ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed for method parameters");

        // Collect all error messages
        List<String> errors = ex.getAllValidationResults().stream()
                .flatMap(vr -> vr.getResolvableErrors().stream())
                .map(error -> error.getDefaultMessage())
                .toList();

        body.put("details", errors);
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("details", errors);
        response.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Missing Request Header");
        body.put("message", String.format("Required header '%s' is missing", ex.getHeaderName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}
