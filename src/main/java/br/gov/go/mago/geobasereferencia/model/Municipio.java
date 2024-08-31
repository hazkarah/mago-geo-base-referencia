package br.gov.go.mago.geobasereferencia.model;

import java.io.Serializable;

import org.hibernate.annotations.SQLRestriction;
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

/**
 * Entidade que representa um Municipio
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LIM_MUNICIPIOS_BR")
@SQLRestriction("CD_MUN != 0")
public class Municipio implements Serializable {

    @Id
    @JsonIgnore
    @Column(name = "OBJECTID")
    
    private Integer id;

    @Column(name = "NM_MUN")
    
    private String nome;

    @Column(name = "SIGLA_UF")
    
    private String uf;

    @Column(name = "CD_MUN")
    
    private Integer codigoIbge;

    @Column(columnDefinition = "Geometry", name = "shape")
    @JsonIgnore
    public Geometry geometria;

    @Override
    public String toString() {
        return String.format("%s - %s", nome, uf);
    }

    public String getGeometriaWKT(){
        if(this.geometria != null){
            return this.getGeometria().toText();
        }else{
            return null;
        }
    }
}
