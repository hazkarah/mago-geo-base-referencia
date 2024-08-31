package br.gov.go.mago.geobasereferencia.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnidadeConservacaoShapefileAtributosDTO extends ShapefileAtributosDTO {

    private Integer objectId;
    private BigDecimal id;
    private String codigoUC;
    private String nome;
    private String jurisdicao;
    private String atoLegal;
    private String grupo;
    private String areaOficial;
    private String categoria;
    private String planoName;
    private String versao;
    private ZonedDateTime dataCadas;
    private String operador;
    private BigDecimal areaCalcu;

}
