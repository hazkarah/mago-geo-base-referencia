package br.gov.go.mago.geobasereferencia.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnidadeConservacaoZonaAmortecimentoShapefileAtributosDTO extends ShapefileAtributosDTO {

    private Integer objectId;
    private Integer id;
    private String codigoUC;
    private String nome;
    private String jurisdicao;
    private String grupo;
    private String planoName;
    private Integer raioKM;
    private String versao;
    private ZonedDateTime dataCadas;
    private String operador;
    private Integer areaCalculo;

}
