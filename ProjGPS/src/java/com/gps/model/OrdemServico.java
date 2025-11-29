package com.gps.model;

/**
 *
 * @author Henry Wallace
 */

import java.util.Date;

public class OrdemServico {
    private int idOS;
    private String numeroOSImpressa;
    private GrupoTrabalho grupo; // Relacionamento com GrupoTrabalho
    private String solicitante;
    private int localAndar;
    private String localPorta;
    private String danoDescricao;
    private String ramal;
    private boolean entregue;
    private Date horaEntrega;
    private Date horaDevolucao;

    // Construtor Vazio
    public OrdemServico() {
        this.grupo = new GrupoTrabalho(); // Inicializa o objeto
    }
    
    // Getters e Setters
    public int getIdOS() {
        return idOS;
    }

    public void setIdOS(int idOS) {
        this.idOS = idOS;
    }

    public String getNumeroOSImpressa() {
        return numeroOSImpressa;
    }

    public void setNumeroOSImpressa(String numeroOSImpressa) {
        this.numeroOSImpressa = numeroOSImpressa;
    }

    public GrupoTrabalho getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoTrabalho grupo) {
        this.grupo = grupo;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public int getLocalAndar() {
        return localAndar;
    }

    public void setLocalAndar(int localAndar) {
        this.localAndar = localAndar;
    }

    public String getLocalPorta() {
        return localPorta;
    }

    public void setLocalPorta(String localPorta) {
        this.localPorta = localPorta;
    }

    public String getDanoDescricao() {
        return danoDescricao;
    }

    public void setDanoDescricao(String danoDescricao) {
        this.danoDescricao = danoDescricao;
    }

    public String getRamal() {
        return ramal;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public Date getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(Date horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public Date getHoraDevolucao() {
        return horaDevolucao;
    }

    public void setHoraDevolucao(Date horaDevolucao) {
        this.horaDevolucao = horaDevolucao;
    }
    
}