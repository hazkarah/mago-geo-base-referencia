package br.gov.go.mago.geobasereferencia.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class TerrasIndigenasShapefileAtributosDTO extends ShapefileAtributosDTO {

    private Integer objectId;
    private String nome;
    private String situacao;
    private String instLegal;
    private String dataInst;
    private String etnia;
    private BigDecimal areaMT;
    private String municipio;
    private Integer populacao;
    private Integer anoPopulacao;
    private String nomeTIAB;
    private String observacao;
    private BigDecimal areaHA;

}
