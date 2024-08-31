package br.gov.go.mago.geobasereferencia.repository;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.gov.go.mago.geobasereferencia.model.GeomorfologiaIbgeMT;

public interface GeomorfologiaIbgeMTRepository extends JpaRepository<GeomorfologiaIbgeMT, Integer> {

    @Query(value = "select * " +
            "    FROM base_referencia_geo.geomorfologia_ibge_go geomorfolo0_" +
            "    WHERE SDO_GEOM.RELATE(sdo_cs.transform(geomorfolo0_.shape,4674),'ANYINTERACT', sdo_cs.transform(:geometry,4674),0.05) = 'TRUE'", nativeQuery = true)
    List<GeomorfologiaIbgeMT> findGeomorfologiaByGeometry(Geometry geometry);

}
