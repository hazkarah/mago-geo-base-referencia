package br.gov.go.mago.geobasereferencia.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.gov.go.mago.geobasereferencia.model.LimiteMunicipios;
import br.gov.go.mago.geobasereferencia.model.Municipio;
import br.gov.go.mago.geobasereferencia.model.dto.GeometriaPontoDTO;
import br.gov.go.mago.geobasereferencia.model.dto.LimiteMunicipioMTDTO;
import br.gov.go.mago.geobasereferencia.model.dto.MunicipioDTO;
import br.gov.go.mago.geobasereferencia.service.MunicipioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = "/api/municipios", path = "/api/municipios")
public class MunicipioController extends DefaultController {

    @Autowired
    private MunicipioService service;

    /**
     * Retorna um {@link Municipio} pelo identificador informado
     *
     * @param codigoIBGE Identificador do recurso
     * @return o município.
     */
    @GetMapping(path = "/{codigoIBGE}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retorna o municipio pelo seu Identificador.", description = "Obter o municipio por Identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "204", description = "Sem retorno de dados."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção no servidor")
    })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> read(@PathVariable Integer codigoIBGE) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.debug(String.format("Método: %s | Parâmetro: %d", methodName, codigoIBGE));
        log.trace("Buscando municipio por codigo IBGE {}", codigoIBGE);
        Municipio municipio = service.readByCodigoIBGE(codigoIBGE);
        HttpHeaders responseHeaders = getHttpHeaders(municipio.getId());
        return ResponseEntity.ok().headers(responseHeaders).body(municipio);
    }

    @GetMapping(path = "{codigoIBGE}/wkt", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retorna o WKT do município pelo Código do IBGE.", description = "Obter o WKT do município pelo Código do IBGE.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "204", description = "Sem retorno de dados."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção no servidor")
    })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> getWKTMunicipio(@PathVariable Integer codigoIBGE) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.debug(String.format("Método: %s | Parâmetro: %d", methodName, codigoIBGE));
        log.trace("Buscando municipio por codigo IBGE {}", codigoIBGE);
        Municipio municipio = service.readByCodigoIBGE(codigoIBGE);
        HttpHeaders responseHeaders = getHttpHeaders(municipio.getId());
        return ResponseEntity.ok().headers(responseHeaders).body(municipio.getGeometriaWKT());
    }

    /**
     * Pesquisa um registro de {@link Municipio} baseado numa descrição
     *
     * @param nome Campo a ser pesquisado
     * @param page Página inicial
     * @param size Tamanho da paginação
     * @return {@link Page<Municipio>}
     */
    @GetMapping(path = "/page", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os municipios em ordem alfabética.", description = "Listar todos os municipios.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "204", description = "Sem retorno de dados."),
            @ApiResponse(responseCode = "206", description = "Existe paginação (a quantidade total é maior que o limit informado)."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "406", description = "Intervalo solicitado inválido."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção no servidor")
    })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> read(@RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        Page<Municipio> list = service.read(nome, PageRequest.of(page, size));
        ResponseHeaderPaginable responseHeaderPaginable = new ResponseHeaderPaginable(page, list);
        responseHeaderPaginable.invoke();
        HttpStatus status = responseHeaderPaginable.getStatus();
        return ResponseEntity.status(status).header(CONTENT_RANGE_HEADER, responseHeaderPaginable.responsePageRange())
                .body(list);
    }

    /**
     * Pesquisa todos os registros de {@link Municipio}
     *
     * @return {@link List<Municipio>}
     */
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os municipios em ordem alfabética.", description = "Listar todos os municipios.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "204", description = "Sem retorno de dados."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção no servidor")
    })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> read(@RequestParam(value = "uf") String uf) {
        return ResponseEntity.ok().body(service.readByUF(uf));
    }

    /**
     * Pesquisa os registros de {@link Municipio} por codigos ibge
     *
     * @return {@link List<Municipio>}
     */
    @GetMapping(path = "/batch", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar os municipios por codigos ibge em ordem alfabética.", description = "Listar todos os municipios.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "204", description = "Sem retorno de dados."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção no servidor")
    })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> read(@RequestParam(value = "codigoIbge") List<Integer> codigosIbge) {
        return ResponseEntity.ok().body(service.findByCodigoIbgeIn(codigosIbge));
    }

    @PostMapping(path = "/within/{codigoIBGE}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verifica se uma coordenada geográfica está dentro de um polígono de município.", description = "recebe um código de IBGE e uma coordenada geográfica")
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> within(@PathVariable String codigoIBGE,
            @RequestBody GeometriaPontoDTO ponto) {
        return ResponseEntity.status(service.within(codigoIBGE, ponto) ? HttpStatus.OK : HttpStatus.NOT_FOUND).build();
    }

    @PutMapping(path = "/intersects", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verifica se uma coordenada geográfica está dentro de um polígono de município", description = "recebe um código de IBGE e uma coordenada geográfica")
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> findLimiteMunicipiosMTByIntersectsGeometry(@RequestBody Geometry geometry) {
        List<LimiteMunicipios> listaLimiteMunicipiosMT = service.findByIntersectsGeometry(geometry);
        if (listaLimiteMunicipiosMT != null) {
            return ResponseEntity
                    .ok(listaLimiteMunicipiosMT.stream().map(LimiteMunicipioMTDTO::new).collect(Collectors.toList()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/find-by-ponto/{latitude}/{longitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Carrega uma lista de municípios associada a um ponto.", description = "recebe uma coordenada geográfica")
    // @AuthorizationTokenNotRequired
    public ResponseEntity<List<MunicipioDTO>> findLimiteMunicipiosMTByPonto(@PathVariable Double latitude,
            @PathVariable Double longitude) {
        List<LimiteMunicipios> listaLimiteMunicipiosMT = service.within(latitude, longitude);
        return ResponseEntity.ok(listaLimiteMunicipiosMT.stream().map(MunicipioDTO::new).collect(Collectors.toList()));
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.OPTIONS).build();
    }
}
