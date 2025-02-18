package com.melocode.videoismaael.entities;

public class Tache {
    private int idTache;
    private String nomTache;
    private String description;
    private String statut;
    private int idProjet;  // Clé étrangère vers Projet

    // Constructeur vide
    public Tache() {}

    // Constructeur avec paramètres
    public Tache(int idTache, String nomTache, String description, String statut, int idProjet) {
        this.idTache = idTache;
        this.nomTache = nomTache;
        this.description = description;
        this.statut = statut;
        this.idProjet = idProjet;
    }

    // Getters et Setters
    public int getIdTache() {
        return idTache;
    }

    public void setIdTache(int idTache) {
        this.idTache = idTache;
    }

    public String getNomTache() {
        return nomTache;
    }

    public void setNomTache(String nomTache) {
        this.nomTache = nomTache;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "idTache=" + idTache +
                ", nomTache='" + nomTache + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", idProjet=" + idProjet +
                '}';
    }
}
