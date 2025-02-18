package com.melocode.videoismaael.services;

import java.util.List;

public interface ICrud<T> {
    void ajouterEntite(T entity);      // Method to add an entity
    List<T> afficherEntite();          // Method to get all entities
    void modifierEntite(T entity);     // Method to modify an entity
    void supprimerEntite(T entity);    // Method to delete an entity
}
