package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Classe représentant un pays, pays d'un film, d'une personne ou d'un lieu (Place).
 */
@Entity
public class Country {

  public Country() {
  }

  /**
   * Identifiant unique du pays.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Libellé du langage.
   * Doit être unique et non nul.
   */
  @Column(nullable = false, unique = true, name ="name", length = 128)
  private String name;

  /**
   * url fournie par le fichier
   */
  @Column(name="url", length = 255)
  private String url;


  @OneToMany(mappedBy = "country")
  private List<Place> places;


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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "Country{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", url='" + url + '\'' +
      '}';
  }

  //----------------------------------------------------- METHODES --------------------------------------------------------


}
