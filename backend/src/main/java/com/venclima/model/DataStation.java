package com.venclima.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

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

    // Getter e Setter
    public String getIdStazione() { return idStazione; }
    public void setIdStazione(String idStazione) { this.idStazione = idStazione; }

    public String getStazione() { return stazione; }
    public void setStazione(String stazione) { this.stazione = stazione; }

    public String getLatDDN() { return latDDN; }
    public void setLatDDN(String latDDN) { this.latDDN = latDDN; }

    public String getLonDDE() { return lonDDE; }
    public void setLonDDE(String lonDDE) { this.lonDDE = lonDDE; }

    public String getNomeAbbr() { return nomeAbbr; }
    public void setNomeAbbr(String nomeAbbr) { this.nomeAbbr = nomeAbbr; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }

}
