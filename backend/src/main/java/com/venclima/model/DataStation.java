package com.venclima.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataStation {

    @JsonProperty("ID_stazione")
    private String idStazione;

    @JsonProperty("stazione")
    private String stazione;

    @JsonProperty("latDDN")
    private String latDDN;

    @JsonProperty("lonDDE")
    private String lonDDE;

    @JsonProperty("nome_abbr")
    private String nomeAbbr;

    @JsonProperty("data")
    private String data;

    @JsonProperty("valore")
    private String valore;

}
