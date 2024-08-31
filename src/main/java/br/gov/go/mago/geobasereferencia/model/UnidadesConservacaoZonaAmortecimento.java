package br.gov.go.mago.geobasereferencia.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa as unidades de conservação de amortecimento.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UC_AMORTECIMENTO")
public class UnidadesConservacaoZonaAmortecimento implements Serializable {

    @Id
    @Column(name = "OBJECTID", updatable = false)
    
    @GeneratedValue(generator = "SEQUnidadesConservacaoZonaAmortecimento", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "SEQUnidadesConservacaoZonaAmortecimento", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    private Integer objectId;

    @Column(name = "ID", updatable = false)
    private Integer id;

    @Column(name = "CODIGO_UC", length = 10)
    @Length(max = 10, message = "O limite do campo é de 10 caracteres.")
    
    private String codigoUC;

    @Column(name = "NOME", length = 150)
    @Length(max = 150, message = "O limite do campo é de 150 caracteres.")
    
    private String nome;

    @Column(name = "JURISDICAO", length = 15)
    @Length(max = 15, message = "O limite do campo é de 15 caracteres.")
    
    private String jurisdicao;

    @Column(name = "GRUPO", length = 30)
    @Length(max = 30, message = "O limite do campo é de 30 caracteres.")
    
    private String grupo;

    @Column(name = "PLANO_MANE", length = 40)
    @Length(max = 40, message = "O limite do campo é de 40 caracteres.")
    
    private String planoName;

    @Column(name = "VERSAO", length = 10)
    @Length(max = 10, message = "O limite do campo é de 10 caracteres.")
    
    private String versao;

    @Getter
    @Column(name = "DATA_CADAS", updatable = false)
    
    private ZonedDateTime dataCadas;

    @Column(name = "OPERADOR", length = 80)
    @Length(max = 80, message = "O limite do campo é de 80 caracteres.")
    
    private String operador;

    @Column(name = "AREA_CALCU", updatable = false)
    private Integer areaCalculo;

    @Column(name = "SHAPE", updatable = false)
    @JsonIgnore
    private Geometry geometry;

}
