package br.gov.go.mago.geobasereferencia.exception;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.operation.valid.TopologyValidationError;

import br.gov.go.mago.car.core.web.exception.BusinessLogicException;

public final class TopologiaBusinessLogicException extends BusinessLogicException {

    public static final String[] errMsg = {"Erro de validação de topologia", "Ponto repetido",
            "O buraco está fora da casca", "Buracos são aninhados", "Interior está desconectado", "Auto-interseção",
            "Auto-interseção do anel", "Conchas aninhadas", "Anéis duplicados",
            "Poucos pontos distintos no componente geometria", "Coordenadas inválidas", "O anel não está fechado"};
    private static final long serialVersionUID = 1L;
    private final int errorType;
    private final Coordinate pt;

    public TopologiaBusinessLogicException(TopologyValidationError topologyValidationError) {
        this.errorType = topologyValidationError.getErrorType();
        this.pt = topologyValidationError.getCoordinate();
    }

    public Coordinate getCoordinate() {
        return pt;
    }

    public int getErrorType() {
        return errorType;
    }

    public String getMessage() {
        return errMsg[errorType];
    }

    private boolean is2D() {
        return pt.getZ() != pt.getZ();
    }

    public String toString() {
        String locStr = "";
        if (pt != null)
            locStr = String.format(" perto ou no ponto (%.6f, %.6f%s)", pt.y, pt.x,
                    (is2D() ? "" : String.format(", %.6f", pt.getZ()))).replace(".", ",") + ".";
        return getMessage() + locStr;
    }
}
