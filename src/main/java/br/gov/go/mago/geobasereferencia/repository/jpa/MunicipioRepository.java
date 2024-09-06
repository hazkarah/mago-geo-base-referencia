package br.gov.go.mago.geobasereferencia.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.go.mago.car.domain.repository.jpa.base.IRepository;
import br.gov.go.mago.geobasereferencia.model.Municipio;

/**
 * Repositório de Municipio
 *
 * @see JpaRepository
 */
@Repository
public interface MunicipioRepository extends IRepository<Municipio, Integer> {

    Page<Municipio> findAllByOrderByNome(Pageable pageable);

    List<Municipio> findByUfOrderByNome(String uf);

    Page<Municipio> findByNomeContainingIgnoreCaseOrderByNome(String nome, Pageable pageable);

    Optional<Municipio> findByCodigoIbge(Integer codigoIBGE);

    Optional<Municipio> findFirstByCodigoIbge(Integer codigoIBGE);

    List<Municipio> findByCodigoIbgeIn(List<Integer> codigosIBGE);
}
