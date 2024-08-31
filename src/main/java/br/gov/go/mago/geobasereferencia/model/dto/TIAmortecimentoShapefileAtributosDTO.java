package br.gov.go.mago.geobasereferencia.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class TIAmortecimentoShapefileAtributosDTO extends ShapefileAtributosDTO {

    private Integer objectId;
    private String nomeTI;
    private String sitJuridica;
    private String instLegal;
    private String dataInst;
    private String etnia;
    private String nomeTIAB;
    private BigDecimal buffDist;

}
