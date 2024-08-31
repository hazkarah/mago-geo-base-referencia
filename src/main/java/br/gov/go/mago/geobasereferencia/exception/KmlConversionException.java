package br.gov.go.mago.geobasereferencia.exception;

public final class KmlConversionException extends RuntimeException {

    public KmlConversionException(String message) {
        super(message);
    }

    public KmlConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
