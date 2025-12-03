package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une langue disponible pour des Films entre autres.
 */
@Entity
public class Language {

  public Language() {
  }

  /**
   * Identifiant unique du langage.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Libellé du langage.
   * Doit être unique et non nul.
   */
  @Column(nullable = false, unique = true, name ="name", length = 255)
  private String name;


  /**
   * Films
   */
  @ManyToMany
  @JoinTable(
    name = "film_language",
    joinColumns = @JoinColumn(name = "language_id"),
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
    return "Language{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }


//----------------------------------------------------- METHODES --------------------------------------------------------

}
