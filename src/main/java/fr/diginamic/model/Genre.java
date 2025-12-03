package fr.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe représentant un genre de film dans la base de données.
 */
@Entity
public class Genre {


  public Genre() {
  }

  /**
   * Identifiant unique du genre et clé primaire dans sa table.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Libellé du genre.
   * Doit être unique et non nul.
   */
  @Column(nullable = false, unique = true, name = "name", length = 255)
  private String name;


  /**
   * Films
   */
  @ManyToMany(mappedBy = "genres")
  private Set<Film> films = new HashSet<>();


  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------


  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Set<Film> getFilms() {
    return films;
  }

  public void setFilms(Set<Film> films) {
    this.films = films;
  }

  @Override
  public String toString() {
    return "Genre{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }

  //----------------------------------------------------- METHODES --------------------------------------------------------




}
