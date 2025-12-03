package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
  @ManyToMany
  @JoinTable(
    name = "film_genre",
    joinColumns = @JoinColumn(name = "genre_id"),
    inverseJoinColumns = @JoinColumn(name = "film_id")
  )
  private List<Film> films = new ArrayList<>();


  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
