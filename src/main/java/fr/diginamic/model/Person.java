package fr.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Inheritance;


import java.time.LocalDate;

/**
 * Classe mère Personne contenant la majorité des attributs de Director et Actor
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

  public Person() {}

  /**
   * Identifiant unique de la personne.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * identifiant IMDB
   */
  @Column(unique = true, name = "imdb_id", length = 16)
  private String imdbId;

  /**
   * prenom + nom de la personne
   */
  @Column(name = "name", nullable = false, length = 255)
  private String name;

  /**
   * date de naissance
   */
  @Column(name= "birth_date")
  private LocalDate birthDate;

  /**
   * Lieu de naissance
   */
  @ManyToOne
  @JoinColumn(name = "place_id", nullable = true)
  private Place birthPlace;

  /**
   * url
   */
  @Column(name = "url",length = 255)
  private String url;


  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getImdbId() {
    return imdbId;
  }

  public void setImdbId(String imdbId) {
    this.imdbId = imdbId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public Place getBirthPlace() {
    return birthPlace;
  }

  public void setBirthPlace(Place birthPlace) {
    this.birthPlace = birthPlace;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "Person{" +
      "id=" + id +
      ", imdbId='" + imdbId + '\'' +
      ", name='" + name + '\'' +
      ", birthDate=" + birthDate +
      '}';
  }


  //----------------------------------------------------- METHODES --------------------------------------------------------





}
