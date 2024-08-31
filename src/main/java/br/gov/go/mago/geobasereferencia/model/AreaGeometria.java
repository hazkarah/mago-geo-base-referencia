package br.gov.go.mago.geobasereferencia.model;



import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "DUAL")

public class AreaGeometria implements Serializable {

    @Id
    @Column(name = "ID")
    
    private Integer id;

    @Column(name = "WKT")
    private String wkt;

    @Column(name = "AREA_M2")
    private BigDecimal areaM2;

    @Column(name = "AREA_HA")
    private BigDecimal areaHa;

    @Column(name = "AREA_KM2")
    private BigDecimal areaKm2;
}
