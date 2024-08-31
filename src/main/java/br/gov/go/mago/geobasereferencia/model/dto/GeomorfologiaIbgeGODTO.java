package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import br.gov.go.mago.geobasereferencia.model.GeomorfologiaIbgeMT;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class GeomorfologiaIbgeGODTO implements Serializable {

    private Integer objectId;

    private String legendaCarga;

    
    private String dominio;

    private String nomeRegiao;

    private Long idUnidade;

    private String nomeUnidade;

    private String letraSimb;

    private String categoria;

    private String natureza;

    private String caracteristica;

    private String forma;

    private String desidadeDreno;

    private String aprofInci;

    private String nivelAlteracao;

    private String legendaSuplementar;

    private String cdLegendaSuplementar;

    private String legenda;

    private Double areaPoligonoKm;

    private String compartimento;

    private Double cdDominio;

    private Double cdUnidID;

    public GeomorfologiaIbgeGODTO(GeomorfologiaIbgeMT geomorfologia) {
        objectId = geomorfologia.getObjectId();
        legendaCarga = geomorfologia.getLegendaCarga();
        dominio = geomorfologia.getNomeDominio();
        nomeRegiao = geomorfologia.getNomeRegiao();
        idUnidade = geomorfologia.getIdUnidade();
        nomeUnidade = geomorfologia.getNomeUnidade();
        letraSimb = geomorfologia.getLetraSimb();
        categoria = geomorfologia.getCategoria();
        natureza = geomorfologia.getNatureza();
        caracteristica = geomorfologia.getCaracteristica();
        forma = geomorfologia.getForma();
        desidadeDreno = geomorfologia.getDesidadeDreno();
        aprofInci = geomorfologia.getAprofInci();
        nivelAlteracao = geomorfologia.getNivelAlteracao();
        legendaSuplementar = geomorfologia.getLegendaSuplementar();
        cdLegendaSuplementar = geomorfologia.getCdLegendaSuplementar();
        legenda = geomorfologia.getLegenda();
        areaPoligonoKm = geomorfologia.getAreaPoligonoKm();
        compartimento = geomorfologia.getCompartimento();
        cdDominio = geomorfologia.getCdDominio();
        cdUnidID = geomorfologia.getCdCompartimentoID();
    }
}
