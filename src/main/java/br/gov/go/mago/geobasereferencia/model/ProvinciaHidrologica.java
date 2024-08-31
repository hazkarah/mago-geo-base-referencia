package br.gov.go.mago.geobasereferencia.model;

import org.locationtech.jts.geom.Geometry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PROVINCIA_HIDROLOGICA")
public class ProvinciaHidrologica {

    @Id
    @Column(name = "OBJECTID")
    private Integer id;

    @Column(name = "NOME")
    private String nome;

    @Column(columnDefinition = "Geometry", name = "shape")
    public Geometry geometry;

}
