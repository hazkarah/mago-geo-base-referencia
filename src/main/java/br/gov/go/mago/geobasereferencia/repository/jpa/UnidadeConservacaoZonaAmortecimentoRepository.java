package br.gov.go.mago.geobasereferencia.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.go.mago.geobasereferencia.model.UnidadesConservacaoZonaAmortecimento;

@Repository
public interface UnidadeConservacaoZonaAmortecimentoRepository extends JpaRepository<UnidadesConservacaoZonaAmortecimento, Integer> {

    @Query(value = " Select * from BASE_REFERENCIA_GEO.UC_AMORTECIMENTO where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674), 'ANYINTERACT', sdo_cs.transform( sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude,NULL),null,null),4674),0.05) = 'TRUE'", nativeQuery = true)
    List<UnidadesConservacaoZonaAmortecimento> findByLongitudeLatitude(Double longitude, Double latitude);
}