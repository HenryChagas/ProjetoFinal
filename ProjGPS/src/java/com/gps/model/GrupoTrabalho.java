package com.gps.model;

/**
 *
 * @author Henry Wallace
 */

public class GrupoTrabalho {
    private int idGrupo;
    private String nomeGrupo;

    // Construtor vazio
    public GrupoTrabalho() {}

    // Construtor completo
    public GrupoTrabalho(int idGrupo, String nomeGrupo) {
        this.idGrupo = idGrupo;
        this.nomeGrupo = nomeGrupo;
    }

    // Getters e Setters
    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }
    
}