package br.gov.go.mago.geobasereferencia.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.xml.MetadataURL;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.wololo.jts2geojson.GeoJSONReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.go.mago.geobasereferencia.config.GeoserverPropertiesConfig;
import br.gov.go.mago.geobasereferencia.model.dto.CamadaDTO;
import br.gov.go.mago.geobasereferencia.model.dto.GeoJSONParamsDTO;
import br.gov.go.mago.geobasereferencia.util.Utils;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/camadas")

public class CamadaController {

    private final GeoserverPropertiesConfig geoserverProperties;

    public CamadaController(
            @Autowired @Qualifier("geoserverPropertiesConfig") GeoserverPropertiesConfig geoserverProperties) {
        this.geoserverProperties = geoserverProperties;
    }

    @Operation(summary ="Carrega uma lista de camadas de um Geoserver por meio de uma URL")
    @GetMapping(path = "listar", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<CamadaDTO>> carregarCamadas(@RequestParam String url) {
        try {
            URL urlPesquisa = new URL(url);
            final WebMapServer wms = new WebMapServer(urlPesquisa);
            WMSCapabilities capabilities = wms.getCapabilities();

            List<CamadaDTO> camadas = getCamadas(capabilities.getLayer(), wms.getInfo().getSource().toString());
            CamadaDTO camadaDTO = new CamadaDTO(capabilities.getLayer(), camadas, wms.getInfo().getSource().toString());

            String organizationName = getTitle(capabilities, wms.getInfo().getSource().toString());
            camadaDTO.setTitle(organizationName);

            return ResponseEntity.ok(Collections.singletonList(camadaDTO));
        } catch (final MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (final ServiceException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private static String getTitle(final WMSCapabilities capabilities, String url) {
        if (StringUtils.isNotBlank(capabilities.getLayer().getTitle()) &&
                !"GeoServer Web Map Service".equalsIgnoreCase(capabilities.getLayer().getTitle())) {
            return capabilities.getLayer().getTitle().trim();
        } else if (capabilities.getService() != null && capabilities.getService().getContactInformation() != null
                && StringUtils.isNotBlank(capabilities.getService().getContactInformation().getOrganisationName())) {
            return capabilities.getService().getContactInformation().getOrganisationName().toString().trim();
        }
        return url;
    }

    @Operation(summary ="Carrega a lista de camadas da MAGO")
    @GetMapping(path = "mago", produces = { MediaType.APPLICATION_JSON_VALUE })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> carregarCamadas() {
        try {
            final String authKeyValue = "&authkey=" + geoserverProperties.getAuthToken();
            final URL url = new URL(geoserverProperties.getUrl() + authKeyValue);
            final WebMapServer wms = new WebMapServer(url);
            final WMSCapabilities capabilities = wms.getCapabilities();
            List<CamadaDTO> camadas = getCamadas(capabilities.getLayer(), url.toString());
            return ResponseEntity.ok(camadas);
        } catch (final RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (final MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (final ServiceException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary ="Retorna URL do Geoserver")
    @GetMapping(path = "geoserver", produces = { MediaType.APPLICATION_JSON_VALUE })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> getGeoserverURL() {
        try {
            final String authKeyValue = "&authkey=" + geoserverProperties.getAuthToken();
            final URL url = new URL(geoserverProperties.getUrl() + authKeyValue);
            Map obj = new HashMap();
            obj.put("url", url.toString());

            return ResponseEntity.ok(obj);
        } catch (final RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (final MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary ="Busca o link do metadados da camada")
    @PostMapping(path = "geojson", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getJsonFromServer(@RequestBody GeoJSONParamsDTO geoJSONParamsDTO) {
        try {
            final HashMap body = getJSON(geoJSONParamsDTO.getUrl());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary ="Busca o link do metadados da camada")
    @GetMapping(path = "metadado", produces = { MediaType.APPLICATION_JSON_VALUE })
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> getMetadado(@RequestParam String camadaName) {
        try {
            Map obj = new HashMap();
            ObjectMapper mapper = new ObjectMapper();
            MetadataURL metadata = new MetadataURL(null, null, null);

            final String authKeyValue = "&authkey=" + geoserverProperties.getAuthToken();
            final URL url = new URL(geoserverProperties.getUrl() + authKeyValue);
            final WebMapServer wms = new WebMapServer(url);
            final WMSCapabilities capabilities = wms.getCapabilities();
            List<Layer> layers = capabilities.getLayerList();

            for (Layer layer : layers) {
                if (layer.getName() != null) {
                    if (Objects.equals(layer.getName(), camadaName)) {
                        List<MetadataURL> metadataList = layer.getMetadataURL();
                        for (MetadataURL meta : metadataList) {
                            if (Objects.equals(meta.getType(), "geonetwork_link")) {
                                metadata = meta;
                            }
                        }
                    }
                }
            }

            if (metadata.getUrl() != null) {
                obj.put("link", metadata.getUrl());
            } else {
                obj.put("link", null);
            }

            String json = mapper.writeValueAsString(obj);
            return ResponseEntity.ok(json);
        } catch (final RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (final MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (final ServiceException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private static List<CamadaDTO> getCamadas(Layer layer, String url) {
        return Arrays.stream(layer.getChildren())
                .map(c -> new CamadaDTO(c, getCamadas(c, url), url))
                .collect(Collectors.toList());

    }

    @Operation(summary ="Carrega as propriedades de um ponto da camada usando o GetFeatureInfo")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping(path = "featureInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> getFeatureInfo(@RequestParam String url) {
        try {
            final HashMap body = getJSON(url);
            final List<Map<String, Object>> valor = getFeatures(body);
            Double area = null;
            for (Map v : valor) {
                LinkedHashMap<String, Object> geometry = (LinkedHashMap) v.remove("geometry");
                if (geometry != null && "Polygon".equalsIgnoreCase((String) geometry.get("type"))) {
                    List<List<List<Double>>> coordinates = (List<List<List<Double>>>) geometry.get("coordinates");
                    double[][][] coordenadas = new double[coordinates.size()][][];
                    for (int x = 0; x < coordinates.size(); x++) {
                        coordenadas[x] = new double[coordinates.get(x).size()][];
                        for (int i = 0; i < coordinates.get(x).size(); i++) {
                            coordenadas[x][i] = new double[coordinates.get(x).get(i).size()];
                            for (int j = 0; j < coordinates.get(x).get(i).size(); j++) {
                                coordenadas[x][i][j] = coordinates.get(x).get(i).get(j);
                            }
                        }
                    }

                    GeoJSONReader reader = new GeoJSONReader();
                    org.wololo.geojson.Polygon point = new org.wololo.geojson.Polygon(coordenadas);
                    Geometry polygon = reader.read(point, new GeometryFactory(new PrecisionModel(), 4674));

                    area = Math.toRadians(polygon.getArea()) * 6371000 * 100;
                }
            }

            if (area != null && !valor.isEmpty()) {
                ((Map) valor.get(0).get("properties")).put("Área", String.format("%f km² ou %f ha", area, area * 100));
            }

            return ResponseEntity.ok(valor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary ="Carrega as as colunas da tabela de atributos de uma camada usando o getJsonFromServer")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping(path = "getjson", produces = MediaType.APPLICATION_JSON_VALUE)
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> getJsonFromServer(@RequestParam String url) {
        try {
            final HashMap body = getJSON(url);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary ="Carrega as colunas da tabela de atributos de uma camada usando o getLayerColumns")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "getrows", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> getRowsJsonFromServer(@RequestParam String url, @RequestParam String cql_filter) {
        try {
            if (!cql_filter.isEmpty()) {
                url += cql_filter;
            }
            final HashMap body = getJSONFromGeoService(url);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static HashMap<String, Object> getJSON(String url) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Geometry> entity = new HttpEntity<>(headers);
        URI urlSearch = UriComponentsBuilder.fromHttpUrl(url).build(true).toUri();

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(urlSearch, HttpMethod.GET, entity, HashMap.class);
            if (response.getStatusCode().value() % 100 == 4) {
                throw new Exception("Serviço indisponível.");
            } else if (response.getStatusCode().value() % 500 == 4 || response.getStatusCode().value() != 200) {
                throw new Exception("Falha interna no serviço remoto de WMS.");
            } else if (response.getBody().containsValue("ServiceException")) {
                throw new Exception("Falha ao carregar a camada do serviço remoto de WFS.");
            }
            return response.getBody();
        } catch (final RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new Exception("Falha no serviço de WMS remoto. Formato de resposta inválido.", e);
            }
            throw new Exception("Falha interna no serviço remoto de WMS.", e);
        }
    }

    private static HashMap<String, Object> getJSONFromGeoService(String url) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Geometry> entity = new HttpEntity<>(headers);

        URI urlSearch = (Utils.containsNonAsciiOrBrazilianChars(url) ? Utils.buildURIWithSpecialCharacters(url)
                : UriComponentsBuilder.fromHttpUrl(url).build(false).toUri());

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(urlSearch, HttpMethod.GET, entity, HashMap.class);
            if (response.getStatusCode().value() % 100 == 4) {
                throw new Exception("Serviço indisponível.");
            } else if (response.getStatusCode().value() % 500 == 4 || response.getStatusCode().value() != 200) {
                throw new Exception("Falha interna no serviço remoto de WMS.");
            }
            return response.getBody();
        } catch (final RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new Exception("Falha no serviço de WMS remoto. Formato de resposta inválido.", e);
            }
            throw new Exception("Falha interna no serviço remoto de WMS.", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<Map<String, Object>> getFeatures(Map<String, Object> body) {
        List<Map<String, Object>> ret = Collections.emptyList();
        if (body != null) {
            if (body.get("features") != null) {
                // Feature collection.
                ret = (List) body.get("features");
            } else if (body.get("properties") != null) {
                // Single feature
                ret = Collections.singletonList(body);
            }
        }
        return ret;
    }
}
