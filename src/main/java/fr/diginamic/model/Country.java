package fr.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

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

  /**
   * les differentes regions du pays où ont été tournés des films
   */
  @OneToMany(mappedBy = "country")
  private Set<Place> places = new HashSet<>();


  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------


  public int getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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


  public Set<Place> getPlaces() {
    return places;
  }

  public void setPlaces(Set<Place> places) {
    this.places = places;
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
