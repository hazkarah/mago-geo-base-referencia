package br.gov.go.mago.geobasereferencia.model;

import java.io.Serializable;

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
 * Entidade que representa HID BACIAS HIDROGRAFICAS}
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HID_BACIAS_HIDROGRAFICAS")

public class BaciaHidrografica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OBJECTID")
    
    @GeneratedValue(generator = "SEQBaciaHidrografica", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "SEQBaciaHidrografica", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    private Integer objectId;

    @Column(name = "BACIA", length = 50, nullable = true)
    @Length(max = 50, message = "O limite do campo BACIA é de 50 caracteres.")
    
    private String bacia;

    @Column(name = "SUB_BACIA", length = 50, nullable = true)
    @Length(max = 50, message = "O limite do campo SUB_BACIA é de 50 caracteres.")
    
    private String subBacia;

    @Column(name = "MICRO_BACI", length = 50, nullable = true)
    @Length(max = 50, message = "O limite do campo MICRO_BACI é de 50 caracteres.")
    
    private String microBacia;

    @Column(name = "CODIGO", length = 50, nullable = true)
    @Length(max = 50, message = "O limite do campo CODIGO é de 50 caracteres.")
    
    private String codigo;

    @Column(name = "UNIDADE", length = 50, nullable = true)
    @Length(max = 50, message = "O limite do campo UNIDADE é de 50 caracteres.")
    
    private String unidade;

    @Column(name = "SHAPE", nullable = true, updatable = false)
    @JsonIgnore
    private Geometry geometry;


}
