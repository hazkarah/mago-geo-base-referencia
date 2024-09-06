package br.gov.go.mago.geobasereferencia.repository.jpa;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.go.mago.geobasereferencia.model.LimiteEstado;

@Repository
public interface LimiteEstadoRepository extends JpaRepository<LimiteEstado, Integer> {

    @Query("select bh from LimiteEstado bh where INTERSECTS(transform(bh.geometry, 4674), transform(:geometry, 4674)) = true")
    List<LimiteEstado> findByIntersectsGeometry(Geometry geometry);

    @Query(value = "select transform(bh.geometry, 4674) from LimiteEstado bh ")
    List<Geometry> findAll4674();
}
