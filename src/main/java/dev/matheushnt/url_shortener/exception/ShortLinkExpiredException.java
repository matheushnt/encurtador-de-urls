package dev.matheushnt.url_shortener.exception;

public class ShortLinkExpiredException extends RuntimeException{
    public ShortLinkExpiredException(String message) {
        super(message);
    }
}
