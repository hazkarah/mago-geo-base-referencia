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
@Table(name = "LIM_MUNICIPIOS_GO")

public class LimiteMunicipios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @Column(name = "OBJECTID")

    private Integer id;

    @Column(name = "MUNICIPIO")

    private String nome;

    @Column(name = "ESTADO")

    private String uf;

    @Column(name = "COD_IBGE")

    private Integer codigoIbge;

    @Column(columnDefinition = "Geometry", name = "shape")
    @JsonIgnore
    public Geometry geometria;
}
