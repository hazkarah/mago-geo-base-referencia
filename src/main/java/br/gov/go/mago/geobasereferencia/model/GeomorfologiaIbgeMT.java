package br.gov.go.mago.geobasereferencia.model;

import java.io.Serializable;

import org.locationtech.jts.geom.Geometry;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GEOMORFOLOGIA_IBGE_MT")

public class GeomorfologiaIbgeMT implements Serializable {

    @Id
    @Column(name = "OBJECTID")
    
    private Integer objectId;

    @Column(name = "ID_ORIGEM")
    private Long idOrigem;

    @Column(name = "CD_FCIM")
    private String cdFCIM;

    @Column(name = "LEG_CARGA")
    private String legendaCarga;

    @Column(name = "NM_DOMINIO")
    private String nomeDominio;

    @Column(name = "NM_REGIAO")
    private String nomeRegiao;

    @Column(name = "ID_UNIDADE")
    private Long idUnidade;

    @Column(name = "NM_UNIDADE")
    private String nomeUnidade;

    @Column(name = "LETRA_SIMB")
    private String letraSimb;

    @Column(name = "CATEGORIA")
    private String categoria;

    @Column(name = "NATUREZA")
    private String natureza;

    @Column(name = "CARACT")
    private String caracteristica;

    @Column(name = "FORMA")
    private String forma;

    @Column(name = "DENS_DREN")
    private String desidadeDreno;

    @Column(name = "APROF_INCI")
    private String aprofInci;

    @Column(name = "NIV_ALT")
    private String nivelAlteracao;

    @Column(name = "LEG_SUP")
    private String legendaSuplementar;

    @Column(name = "CD_LEG_SUP")
    private String cdLegendaSuplementar;

    @Column(name = "LEGENDA")
    private String legenda;

    @Column(name = "AR_POLI_KM")
    private Double areaPoligonoKm;

    @Column(name = "COMPARTIME")
    private String compartimento;

    @Column(name = "CD_COMP_ID")
    private Double cdCompartimentoID;

    @Column(name = "CD_DOMINIO")
    private Double cdDominio;

    @Column(name = "CD_UNID_ID")
    private Double cdUnidadeID;

    @JsonIgnore
    @Column(name = "SHAPE")
    private Geometry geometry;
}
