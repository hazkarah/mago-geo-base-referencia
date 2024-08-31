package br.gov.go.mago.geobasereferencia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.wololo.jts2geojson.GeoJSONReader;

import br.gov.go.mago.geobasereferencia.enumerator.EnumLayerAnaliseDeInterseccao;
import br.gov.go.mago.geobasereferencia.enumerator.EnumMensagensInterseccaoCamadas;
import br.gov.go.mago.geobasereferencia.model.AreasUsoRestrito;
import br.gov.go.mago.geobasereferencia.model.BaciaHidrografica;
import br.gov.go.mago.geobasereferencia.model.GeomorfologiaIbgeMT;
import br.gov.go.mago.geobasereferencia.model.ProvinciaHidrologica;
import br.gov.go.mago.geobasereferencia.model.TIAmortecimento;
import br.gov.go.mago.geobasereferencia.model.TerrasIndigenas;
import br.gov.go.mago.geobasereferencia.model.UnidadesConservacao;
import br.gov.go.mago.geobasereferencia.model.UnidadesConservacaoZonaAmortecimento;
import br.gov.go.mago.geobasereferencia.model.dto.GeometriaDTO;
import br.gov.go.mago.geobasereferencia.model.dto.ResultadoRestricaoPontoGeo;
import br.gov.go.mago.geobasereferencia.model.dto.ValidacaoPontoDTO;
import br.gov.go.mago.geobasereferencia.repository.AreasUsoRestritoRepository;
import br.gov.go.mago.geobasereferencia.repository.BaciaHidrograficaRepository;
import br.gov.go.mago.geobasereferencia.repository.GeomorfologiaIbgeMTRepository;
import br.gov.go.mago.geobasereferencia.repository.ProvinciaHidrologicaRepository;
import br.gov.go.mago.geobasereferencia.repository.TIAmortecimentoRepository;
import br.gov.go.mago.geobasereferencia.repository.TerrasIndigenasRepository;
import br.gov.go.mago.geobasereferencia.repository.UnidadeConservacaoRepository;
import br.gov.go.mago.geobasereferencia.repository.UnidadeConservacaoZonaAmortecimentoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
@AllArgsConstructor
public class GeoInterseccaoCamadasService {

    private final TerrasIndigenasRepository terrasIndigenasRepository;

    private final BaciaHidrograficaRepository baciaHidrograficaRepository;

    private final TIAmortecimentoRepository tiAmortecimentoRepository;

    private final UnidadeConservacaoRepository unidadeConservacaoRepository;

    private final UnidadeConservacaoZonaAmortecimentoRepository unidadeConservacaoZonaAmortecimentoRepository;

    private final GeomorfologiaIbgeMTRepository geomorfologiaIbgeMTRepository;

    private final AreasUsoRestritoRepository areasUsoRestritoRepository;

    private final ProvinciaHidrologicaRepository provinciaHidrologicaRepository;

    public List<ResultadoRestricaoPontoGeo> confirmaInterseccao(ValidacaoPontoDTO validacaoPontoDTO) {

        List<ResultadoRestricaoPontoGeo> resultado = new ArrayList<>();

        validacaoPontoDTO.getListaPonto().forEach(ponto -> validacaoPontoDTO.getListaValidacoes().forEach(validacao -> {
            if (validacao == EnumLayerAnaliseDeInterseccao.BACIAS_HIDROGRAFICAS) {
                List<BaciaHidrografica> baciaHidrografica =
                        baciaHidrograficaRepository.findBaciaByGeometryNativeQuery(ponto.getLongitude(), ponto.getLatitude());
                if (!baciaHidrografica.isEmpty()) {
                    resultado.add(new ResultadoRestricaoPontoGeo(true, EnumMensagensInterseccaoCamadas.MENSAGEM59.getDescricao() + " " + baciaHidrografica.get(0).getBacia(), validacao));
                } else {
                    resultado.add(new ResultadoRestricaoPontoGeo(false, "Nada consta em " + EnumLayerAnaliseDeInterseccao.BACIAS_HIDROGRAFICAS.getDescricao(), validacao));
                }
            }
            if (validacao == EnumLayerAnaliseDeInterseccao.TERRAS_INDIGENAS) {
                List<TerrasIndigenas> listaTI;
                if (ponto.getWkt() != null && !ponto.getWkt().trim().isEmpty()) {
                    listaTI = terrasIndigenasRepository.findTerrasIndigenasByGeometryNativeQuery(ponto.getGeometry());
                } else {
                    listaTI = terrasIndigenasRepository.findTerrasIndigenasByGeometryNativeQuery(ponto.getLongitude(), ponto.getLatitude());
                }
                if (!listaTI.isEmpty()) {
                    resultado.add(new ResultadoRestricaoPontoGeo(true, EnumMensagensInterseccaoCamadas.MENSAGEM53.getDescricao() + " " + listaTI.get(0).getNome(), validacao));
                } else {
                    resultado.add(new ResultadoRestricaoPontoGeo(false, "Nada consta em " + EnumLayerAnaliseDeInterseccao.TERRAS_INDIGENAS.getDescricao(), validacao));
                }
            }
            if (validacao == EnumLayerAnaliseDeInterseccao.TI_AMORTECIMENTO_BUFFER) {
                List<TIAmortecimento> listtiAmortecimento;
                if (ponto.getWkt() != null && !ponto.getWkt().trim().isEmpty()) {
                    listtiAmortecimento = tiAmortecimentoRepository.findByGeometry(ponto.getGeometry());
                } else {
                    listtiAmortecimento = tiAmortecimentoRepository.findByGeometry(ponto.getLongitude(), ponto.getLatitude());
                }
                if (!listtiAmortecimento.isEmpty()) {
                    resultado.add(new ResultadoRestricaoPontoGeo(true, EnumMensagensInterseccaoCamadas.MENSAGEM54.getDescricao() + " " + listtiAmortecimento.get(0).getNomeTI(), validacao));
                } else {
                    resultado.add(new ResultadoRestricaoPontoGeo(false, "Nada consta em " + EnumLayerAnaliseDeInterseccao.TI_AMORTECIMENTO_BUFFER.getDescricao(), validacao));
                }
            }
            if (validacao == EnumLayerAnaliseDeInterseccao.TI_AMORTECIMENTO) {
                List<TIAmortecimento> listtiAmortecimento =
                        tiAmortecimentoRepository.findTIAmortecimentoByGeometryNativeQuery(ponto.getLongitude(), ponto.getLatitude());
                if (!listtiAmortecimento.isEmpty()) {
                    resultado.add(new ResultadoRestricaoPontoGeo(true, EnumMensagensInterseccaoCamadas.MENSAGEM54.getDescricao() + " " + listtiAmortecimento.get(0).getNomeTI(), validacao));
                } else {
                    resultado.add(new ResultadoRestricaoPontoGeo(false, "Nada consta em " + EnumLayerAnaliseDeInterseccao.TI_AMORTECIMENTO.getDescricao(), validacao));
                }
            }
            if (validacao == EnumLayerAnaliseDeInterseccao.UNIDADES_CONSERVACAO_ZONA_AMORTECIMENTO_UC) {
                List<UnidadesConservacaoZonaAmortecimento> listTiAmortecimento =
                        unidadeConservacaoZonaAmortecimentoRepository.findByLongitudeLatitude(ponto.getLongitude(), ponto.getLatitude());
                if (!listTiAmortecimento.isEmpty()) {
                    resultado.add(new ResultadoRestricaoPontoGeo(true, EnumMensagensInterseccaoCamadas.MENSAGEM56.getDescricao() + " " + listTiAmortecimento.get(0).getNome(), validacao));
                } else {
                    resultado.add(new ResultadoRestricaoPontoGeo(false, "Nada consta em " + EnumLayerAnaliseDeInterseccao.TI_AMORTECIMENTO.getDescricao(), validacao));
                }
            }
            if (validacao == EnumLayerAnaliseDeInterseccao.UNIDADES_CONSERVACAO) {
                List<UnidadesConservacao> unidadesConservacao;
                if (ponto.getWkt() != null && !ponto.getWkt().trim().isEmpty()) {
                    unidadesConservacao = unidadeConservacaoRepository.findByLongitudeLatitude(ponto.getGeometry());
                } else {
                    unidadesConservacao = unidadeConservacaoRepository.findByLongitudeLatitude(ponto.getLongitude(), ponto.getLatitude());
                }
                if (!unidadesConservacao.isEmpty()) {
                    String categoria = unidadesConservacao.stream().map(UnidadesConservacao::getCategoria).collect(Collectors.joining(","));
                    if (unidadesConservacao.stream().anyMatch(u -> u.getGrupo().toUpperCase(Locale.ROOT).contains("PROTEÇÃO"))) {
                        resultado.add(new ResultadoRestricaoPontoGeo(true, EnumMensagensInterseccaoCamadas.MENSAGEM58.getDescricao() + " " + unidadesConservacao.get(0).getNome(),
                                categoria, validacao));
                    } else {
                        resultado.add(new ResultadoRestricaoPontoGeo(true, EnumMensagensInterseccaoCamadas.MENSAGEM56.getDescricao() + " " + unidadesConservacao.get(0).getNome(),
                                categoria, validacao));
                    }
                } else {
                    resultado.add(new ResultadoRestricaoPontoGeo(false, "Nada consta em " + EnumLayerAnaliseDeInterseccao.UNIDADES_CONSERVACAO.getDescricao(), validacao));
                }
            }
        }));

        return resultado;
    }

    public List<GeomorfologiaIbgeMT> confirmaInterseccaoGeologia(Double latitude, Double longitude) {
        GeoJSONReader reader = new GeoJSONReader();
        org.wololo.geojson.Point point = new org.wololo.geojson.Point(new double[]{longitude, latitude});
        Geometry geometry = reader.read(point, new GeometryFactory(new PrecisionModel(), 4674));
        return geomorfologiaIbgeMTRepository.findGeomorfologiaByGeometry(geometry);
    }

    public List<GeometriaDTO> recuperaAreasRestritas(Double latitude, Double longitude) {
        List<UnidadesConservacao> unidadesConservacao = unidadeConservacaoRepository.findByLongitudeLatitude(longitude, latitude);
        List<UnidadesConservacaoZonaAmortecimento> listTiAmortecimento = unidadeConservacaoZonaAmortecimentoRepository.findByLongitudeLatitude(longitude, latitude);
        List<AreasUsoRestrito> listAreasUsoRestrito = areasUsoRestritoRepository.findByLongitudeLatitude(longitude, latitude);

        List<GeometriaDTO> ret = new ArrayList<>();
        ret.addAll(unidadesConservacao.stream().map(unidades -> {
            GeometriaDTO dto = new GeometriaDTO();
            dto.setDescricao(unidades.getNome());
            return dto;
        }).collect(Collectors.toList()));
        ret.addAll(listTiAmortecimento.stream().map(ti -> {
            GeometriaDTO dto = new GeometriaDTO();
            dto.setDescricao(ti.getNome());
            return dto;
        }).collect(Collectors.toList()));
        ret.addAll(listAreasUsoRestrito.stream().map(areas -> {
            GeometriaDTO dto = new GeometriaDTO();
            dto.setDescricao(areas.getNome());
            return dto;
        }).collect(Collectors.toList()));
        return ret;
    }

    public List<GeometriaDTO> recuperaProvinciasHidrologicas(Double latitude, Double longitude) {
        List<ProvinciaHidrologica> provinciaHidrologicas = provinciaHidrologicaRepository.findByLongitudeLatitude(longitude, latitude);

        return provinciaHidrologicas.stream().map(unidades -> {
            GeometriaDTO dto = new GeometriaDTO();
            dto.setDescricao(unidades.getNome());
            return dto;
        }).collect(Collectors.toList());
    }
}


