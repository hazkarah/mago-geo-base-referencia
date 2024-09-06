package br.gov.go.mago.geobasereferencia.repository.jpa;

import br.gov.go.mago.geobasereferencia.model.AreaGeometria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GeometriaRepository extends JpaRepository<AreaGeometria, Integer> {
    @Query(value = "WITH GEOMETRIA AS ( SELECT :wkt AS WKT FROM dual ) SELECT 1 AS ID, WKT, SDO_GEOM.SDO_AREA(SDO_GEOMETRY(WKT, 4674), 0.0001, 'unit=hectare') AS AREA_HA, SDO_GEOM.SDO_AREA(SDO_GEOMETRY(WKT, 4674), 0.0001, 'unit=SQ_M')  AS AREA_M2, SDO_GEOM.SDO_AREA(SDO_GEOMETRY(WKT, 4674), 0.0001, 'unit=SQ_KM')  AS AREA_KM2 FROM GEOMETRIA", nativeQuery = true)
    AreaGeometria calculaArea(@Param("wkt") String wkt);
}
