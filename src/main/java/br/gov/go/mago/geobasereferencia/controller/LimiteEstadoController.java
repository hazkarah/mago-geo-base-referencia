package br.gov.go.mago.geobasereferencia.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.go.mago.geobasereferencia.model.LimiteEstado;
import br.gov.go.mago.geobasereferencia.model.dto.LimiteEstadoDTO;
import br.gov.go.mago.geobasereferencia.service.LimiteEstadoService;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/feicao-ponto-atividade")
public class LimiteEstadoController {

    @Autowired
    private LimiteEstadoService service;

    @PutMapping(path = "/interseccao-limite_estado", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retorna um polígono de município no qual o mesmo intersecta com uma coordenada.",
            description = "Recebe uma coordenada geográfica e retorna um polígono")
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> findLimiteEstadoByIntersectsGeometry(@RequestBody Geometry geometry) {
        List<LimiteEstado> listaLimitesEstadoMT = service.findByIntersectsGeometry(geometry);
        if (listaLimitesEstadoMT != null) {
            return ResponseEntity.ok(listaLimitesEstadoMT.stream().map(LimiteEstadoDTO::new).collect(Collectors.toList()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/wkt", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retorna um WKT do estado de GO", description = "Retorna um WKT do estado de GO")    // @AuthorizationTokenNotRequired
    public ResponseEntity<String> findAll() {
        return service.findAll4674().stream()
                .map(Geometry::toText)
                .map(s-> s.replaceAll("\"", ""))
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseGet(()-> ResponseEntity.notFound().build());
    }
}
