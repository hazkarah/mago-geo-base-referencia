package br.gov.go.mago.geobasereferencia.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.go.mago.geobasereferencia.model.dto.GeometriaDTO;
import br.gov.go.mago.geobasereferencia.model.dto.GeomorfologiaIbgeGODTO;
import br.gov.go.mago.geobasereferencia.model.dto.ResultadoRestricaoPontoGeo;
import br.gov.go.mago.geobasereferencia.model.dto.ValidacaoPontoDTO;
import br.gov.go.mago.geobasereferencia.service.GeoInterseccaoCamadasService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/interseccao-camadas")

public class GeoInterseccaoCamadasController {

    private final GeoInterseccaoCamadasService service;

    @Autowired
    public GeoInterseccaoCamadasController(GeoInterseccaoCamadasService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verifica a intersecção de uma lista de pontos em relação a uma lista de camadas.", description = "Recebe uma lista de coordenadas geográficas e a relação de camadas a verificar")
    // @AuthorizationTokenNotRequired
    public ResponseEntity<List<ResultadoRestricaoPontoGeo>> getMensagensInterseccaoPorGeometry(
            @RequestBody ValidacaoPontoDTO lista) {
        return ResponseEntity.ok(service.confirmaInterseccao(lista));
    }

    @Operation(summary = "Recupera a lista de geologias que fazem interseção com o ponto informado.", description = "List")
    @GetMapping(value = "/geologia/{latitude}/{longitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    // @AuthorizationTokenNotRequired
    public ResponseEntity<List<GeomorfologiaIbgeGODTO>> getGeologias(@PathVariable Double latitude,
            @PathVariable Double longitude) {
        log.debug("getGeologias", latitude, longitude);
        List<GeomorfologiaIbgeGODTO> ret = service.confirmaInterseccaoGeologia(latitude, longitude).stream()
                .map(GeomorfologiaIbgeGODTO::new).collect(Collectors.toList());

        if (ret.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ret);
    }

    @Operation(summary = "Recupera a lista de areas restritas.", description =  "List")
    @GetMapping(value = "/areas-restritas/{latitude}/{longitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    // @AuthorizationTokenNotRequired
    public ResponseEntity<List<GeometriaDTO>> getAreasRestritas(@PathVariable Double latitude,
            @PathVariable Double longitude) {
        List<GeometriaDTO> ret = service.recuperaAreasRestritas(latitude, longitude);

        if (ret.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ret);
    }

    @Operation(summary = "Recupera a lista de areas restritas.", description =  "List")
    @GetMapping(value = "/provincias-hidrologicas/{latitude}/{longitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    // @AuthorizationTokenNotRequired
    public ResponseEntity<List<GeometriaDTO>> getProvinciasHidrologicas(@PathVariable Double latitude,
            @PathVariable Double longitude) {
        List<GeometriaDTO> ret = service.recuperaProvinciasHidrologicas(latitude, longitude);

        if (ret.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ret);
    }
}
