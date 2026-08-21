package dev.matheushnt.url_shortener.exception;

import dev.matheushnt.url_shortener.dto.ErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_URL = "https://api.url_shortener.com/problems/";

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        problemDetail.setType(URI.create(BASE_URL + "validation-error"));
        problemDetail.setTitle(HttpStatus.UNPROCESSABLE_CONTENT.name());
        problemDetail.setDetail("Um ou mais campos são inválidos");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(exception = InvalidUrlException.class)
    public ProblemDetail handleInvalidUrlException(InvalidUrlException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        problemDetail.setType(URI.create(BASE_URL + "invalid-url"));
        problemDetail.setTitle(HttpStatus.UNPROCESSABLE_CONTENT.name());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(exception = ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create(BASE_URL + "short-link-not-found"));
        problemDetail.setTitle(HttpStatus.NOT_FOUND.name());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(exception = ShortLinkExpiredException.class)
    public ProblemDetail handleShortLinkExpiredException(ShortLinkExpiredException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GONE);
        problemDetail.setType(URI.create(BASE_URL + "short-link-has-expired"));
        problemDetail.setTitle(HttpStatus.GONE.name());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(exception = {BadCredentialsException.class, UsernameNotFoundException.class})
    public ProblemDetail handleAuthenticationFailure(HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setType(URI.create(BASE_URL + "bad-credentials"));
        problemDetail.setTitle(HttpStatus.UNAUTHORIZED.name());
        problemDetail.setDetail("Credenciais inválidas");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(exception = ResourceFoundException.class)
    public ProblemDetail handleResourceFoundException(ResourceFoundException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setType(URI.create(BASE_URL + "conflict"));
        problemDetail.setTitle(HttpStatus.CONFLICT.name());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(exception = HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create(BASE_URL + "invalid-data"));
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.name());
        problemDetail.setDetail("O corpo da requisição está malformado");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

}
