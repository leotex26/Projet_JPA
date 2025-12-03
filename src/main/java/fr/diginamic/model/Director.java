package fr.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import java.util.HashSet;
import java.util.Set;

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
  private Set<Film> films = new HashSet<>();

  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------

  public Set<Film> getFilms() {
    return films;
  }

  public void setFilms(Set<Film> films) {
    this.films = films;
  }


  //----------------------------------------------------- METHODES --------------------------------------------------------------

}
