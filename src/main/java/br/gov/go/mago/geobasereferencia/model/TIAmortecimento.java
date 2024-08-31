package br.gov.go.mago.geobasereferencia.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
import lombok.NoArgsConstructor;

/**
 * Entidade que representa TI_AMORTECIMENTO}
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TI_AMORTECIMENTO")
public class TIAmortecimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OBJECTID")
    
    @GeneratedValue(generator = "SEQTIAmortecimento", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "SEQTIAmortecimento", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    private Integer objectId;

    @Column(name = "NOMETI", length = 150, nullable = true)
    @Length(max = 150, message = "O limite do campo é de 150 caracteres.")
    
    private String nomeTI;

    @Column(name = "SITJURIDIC", length = 150, nullable = true)
    @Length(max = 150, message = "O limite do campo é de 150 caracteres.")
    
    private String sitJuridica;

    @Column(name = "INST_LEGAL", length = 150, nullable = true)
    @Length(max = 150, message = "O limite do campo é de 150 caracteres.")
    
    private String instLegal;

    @Column(name = "DATA_INST", length = 50, nullable = true)
    @Length(max = 50, message = "O limite do campo é de 50 caracteres.")
    
    private String dataInst;

    @Column(name = "ETNIA", length = 200, nullable = true)
    @Length(max = 200, message = "O limite do campo é de 200 caracteres.")
    
    private String etnia;

    @Column(name = "NOME_TI_AB", length = 150, nullable = true)
    @Length(max = 150, message = "O limite do campo é de 150 caracteres.")
    
    private String nomeTIAB;

    @Column(name = "BUFF_DIST", nullable = true, updatable = false)
    private BigDecimal buffDist;

    @Column(name = "SHAPE", nullable = true, updatable = false)
    @JsonIgnore
    private Geometry geometry;
















}
