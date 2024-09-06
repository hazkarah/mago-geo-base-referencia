package br.gov.go.mago.geobasereferencia.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.Geometry;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "LIM_ESTADO_GO")

public class LimiteEstado implements Serializable {

    @Id
    @Column(name = "OBJECTID")

    @GeneratedValue(generator = "SEQLimiteEstado", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "SEQLimiteEstado", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    private Integer objectId;

    @Column(name = "ESTADO", length = 50)
    private String estado;

    @Column(name = "UF", length = 50)
    private String uf;

    @Column(name = "REGIAO", length = 50)
    private String regiao;

    @Column(name = "PAIS", length = 50)
    private String pais;

    @Column(name = "AREA")
    private BigDecimal area;

    @Column(name = "SHAPE", updatable = false)
    @JsonIgnore
    private Geometry geometry;

}
