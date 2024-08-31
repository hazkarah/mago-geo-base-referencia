package br.gov.go.mago.geobasereferencia.exception;

public final class ShapefileConversionException extends RuntimeException {

    public ShapefileConversionException(String message) {
        super(message);
    }

    public ShapefileConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
