package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité Director (Realisateur) correspondant à la meme table en base
 * PS : la majorité des attributs liés au réalisateur se trouve dans la classe mère Person dont l'identifiant.
 */
@Entity
public class Director extends Person {
  public Director() {}

  /**
   * les diffèrents films qu'il a realisés
   */
  @ManyToMany
  @JoinTable(
    name = "film_director",
    joinColumns = @JoinColumn(name = "director_id"),
    inverseJoinColumns = @JoinColumn(name = "film_id")
  )
  private List<Film> films = new ArrayList<>();

  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------

  public List<Film> getFilms() {
    return films;
  }

  public void setFilms(List<Film> films) {
    this.films = films;
  }



  //----------------------------------------------------- METHODES --------------------------------------------------------------

}
