package br.gov.go.mago.geobasereferencia.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EnumLayerAnaliseDeInterseccao {

    TERRAS_INDIGENAS("Terra Indígena "),
    TI_AMORTECIMENTO_BUFFER("Zona de Amortecimento de Terra Indígena (10km)"),
    TI_AMORTECIMENTO("Zona de Amortecimento de Terra Indígena"),
    UNIDADES_CONSERVACAO("Unidade de Conservação"),
    UNIDADES_CONSERVACAO_ZONA_AMORTECIMENTO_UC("Zona de Amortecimeto de Unidade de Conservação"),
    BACIAS_HIDROGRAFICAS("Bácias Hidrograficas");

    @Getter
    private final String descricao;

}
