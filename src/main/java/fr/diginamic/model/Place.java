package fr.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;


import java.util.HashSet;
import java.util.Set;

/**
 * Classe Endroit qui reference un pays, sert de lieu de naissance ou de tournage
 */
@Entity
public class Place {
  public Place() {}

  /**
   * Identifiant de l'entité Place
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * nom > région ( ou état dans le cas des états-unis ) + ville
   */
  @Column(length = 255, name = "name")
  private String name;

  /**
   * pays
   */
  @ManyToOne
  @JoinColumn( name = "country_id")
  private Country country;

  /**
   * Tout les films tournés à cet endroit
   */
  @ManyToMany
  @JoinTable(
    name = "film_place",
    joinColumns = @JoinColumn(name = "place_id"),
    inverseJoinColumns = @JoinColumn(name = "film_id")
  )
  private Set<Film> films = new HashSet<>();

  /**
   * Toutes les personnes nées ici
   */
  @OneToMany(mappedBy = "birth_place")
  private Set<Person> peopleBornHere = new HashSet<>();

  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public Set<Film> getFilms() {
    return films;
  }

  public void setFilms(Set<Film> films) {
    this.films = films;
  }

  public Set<Person> getPeopleBornHere() {
    return peopleBornHere;
  }

  public void setPeopleBornHere(Set<Person> peopleBornHere) {
    this.peopleBornHere = peopleBornHere;
  }

  @Override
  public String toString() {
    return "Place{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", country=" + country.getName() +
      '}';
  }

  //----------------------------------------------------- METHODES --------------------------------------------------------


}
