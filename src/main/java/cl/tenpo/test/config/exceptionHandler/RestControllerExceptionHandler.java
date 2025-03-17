package cl.tenpo.test.config.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Collections;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestControllerExceptionHandler {

    private static final Log LOGGER = LogFactory.getLog(RestControllerExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = {IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleIOException(IOException ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = {ServletException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleServletException(ServletException ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(ex.getMessage()));
    }


    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LOGGER.error(ex.getMessage());
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(value = {MissingServletRequestPartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handlerMissingServletRequestPartException(MissingServletRequestPartException ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingParameterException(MissingServletRequestParameterException ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ex.getMessage()));
    }


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleNotReadable(HttpMessageNotReadableException ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFound(EntityNotFoundException ex) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList(ex.getMessage()));
    }

}
