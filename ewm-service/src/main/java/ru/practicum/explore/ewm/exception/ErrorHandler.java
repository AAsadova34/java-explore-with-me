package ru.practicum.explore.ewm.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;
import static ru.practicum.explore.ewm.utility.Logger.logWarnException;

@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(BAD_REQUEST) //400
    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, MissingRequestHeaderException.class,
            ConstraintViolationException.class})
    public ErrorResponse handleValidationException(Exception e) {
        logWarnException(e);

        String reason = "Incorrectly made request.";
        String message = "";
        if (e instanceof ValidationException) {
            message = e.getMessage();
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException eValidation = (MethodArgumentNotValidException) e;
            message = Objects.requireNonNull(eValidation.getBindingResult().getFieldError()).getDefaultMessage();
        } else if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException eMissingServletRequestParameter = (MissingServletRequestParameterException) e;
            message = eMissingServletRequestParameter.getLocalizedMessage();
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException eHttpRequestMethod = (HttpRequestMethodNotSupportedException) e;
            message = eHttpRequestMethod.getLocalizedMessage();
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException eMethodArgumentTypeMismatch = (MethodArgumentTypeMismatchException) e;
            message = eMethodArgumentTypeMismatch.getLocalizedMessage();
        }

        return getErrorResponse(BAD_REQUEST, reason, message);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        logWarnException(e);

        String reason = "The required object was not found.";
        return getErrorResponse(NOT_FOUND, reason, e.getMessage());
    }

    @ResponseStatus(CONFLICT) //409
    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    public ErrorResponse handleConflictException(Exception e) {
        logWarnException(e);

        String reason = "";
        String message = "";
        if (e instanceof ConflictException) {
            reason = ((ConflictException) e).getReason();
            message = e.getMessage();
        } else if (e instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException eDataIntegrityViolation = (DataIntegrityViolationException) e;
            reason = "Integrity constraint has been violated.";
            message = eDataIntegrityViolation.getMostSpecificCause().getLocalizedMessage();
        }
        return getErrorResponse(CONFLICT, reason, message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse handleThrowable(final Throwable e) {
        logWarnException(e);

        String message = "An unexpected error has occurred";
        String reason = "Unknown reason";
        return getErrorResponse(INTERNAL_SERVER_ERROR, reason, message);
    }

    private ErrorResponse getErrorResponse(HttpStatus status, String reason, String message) {
        return new ErrorResponse()
                .setStatus(status.name())
                .setReason(reason)
                .setMessage(message)
                .setTimestamp(LocalDateTime.now());
    }

}
