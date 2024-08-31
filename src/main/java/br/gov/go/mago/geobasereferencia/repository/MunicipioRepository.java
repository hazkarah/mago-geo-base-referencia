package br.gov.go.mago.geobasereferencia.repository;

import br.gov.go.mago.geobasereferencia.model.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de Municipio
 *
 * @see JpaRepository
 */
@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    Page<Municipio> findAllByOrderByNome(Pageable pageable);

    List<Municipio> findByUfOrderByNome(String uf);

    Page<Municipio> findByNomeContainingIgnoreCaseOrderByNome(String nome, Pageable pageable);

    Optional<Municipio> findByCodigoIbge(Integer codigoIBGE);

    Optional<Municipio> findFirstByCodigoIbge(Integer codigoIBGE);

    List<Municipio> findByCodigoIbgeIn(List<Integer> codigosIBGE);
}
