package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité Film correspondant à la meme table en base
 */
@Entity
public class Film {

  public Film() {}

  /**
   * identifiant de film
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * identifiant imdb
   */
  @Column(unique = true, length = 16, name = "imdb_id")
  private String imdbId;

  /**
   * libelle
   */
  @Column(length = 512, nullable = false, name = "title")
  private String title;

  /**
   * année de début de tournage
   */
  @Column(name = "year_start")
  private Integer yearStart;

  /**
   * année de fin de tournage
   */
  @Column(name = "year_end")
  private Integer yearEnd;

  /**
   * années tel qu'envoyé par le csv
   */
  @Column(length = 15, nullable = true, name = "year_raw")
  private String yearRaw;

  /**
   * note donnée au film
   */
  @Column(precision = 3, scale = 1)
  private Double rating;

  /**
   * résumé
   */
  @Lob
  @Column(name = "summary")
  private String summary;

  /**
   * url
   */
  @Column(length = 255)
  private String url;

  /**
   * Comprend une liste de tout les Places de tournages lié à l'instance du Film
   */
  @ManyToMany
  @JoinTable(
    name = "film_place",
    joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "place_id")
  )
  private List<Place> filmingLocations = new ArrayList<>();

  /**
   * le pays de production
   */
  @ManyToOne
  @JoinColumn(name = "producer_country_id")
  private Country producerCountry;

  /**
   * Genres
   */
  @ManyToMany
  @JoinTable(
    name = "film_genre",
    joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  private List<Genre> genres = new ArrayList<>();

  /**
   * Langues
   */
  @ManyToMany
  @JoinTable(
    name = "film_language",
    joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "language_id")
  )
  private List<Language> languages = new ArrayList<>();

  /**
   * tout les réalisateurs ayant opéré sur le film
   */
  @ManyToMany
  @JoinTable(
    name = "film_director",
    joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "director_id")
  )
  private List<Director> directors = new ArrayList<>();


  /**
   * liste des plus grands roles du films
   */
  @OneToMany(mappedBy = "film")
  private List<Role> roles = new ArrayList<>();



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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getYearStart() {
    return yearStart;
  }

  public void setYearStart(Integer yearStart) {
    this.yearStart = yearStart;
  }

  public Integer getYearEnd() {
    return yearEnd;
  }

  public void setYearEnd(Integer yearEnd) {
    this.yearEnd = yearEnd;
  }

  public String getYearRaw() {
    return yearRaw;
  }

  public void setYearRaw(String yearRaw) {
    this.yearRaw = yearRaw;
  }

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public List<Place> getFilmingLocations() {
    return filmingLocations;
  }

  public void setFilmingLocations(List<Place> filmingLocations) {
    this.filmingLocations = filmingLocations;
  }

  public Country getProducerCountry() {
    return producerCountry;
  }

  public void setProducerCountry(Country producerCountry) {
    this.producerCountry = producerCountry;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public void setGenres(List<Genre> genres) {
    this.genres = genres;
  }

  public List<Language> getLanguages() {
    return languages;
  }

  public void setLanguages(List<Language> languages) {
    this.languages = languages;
  }

  public List<Director> getDirectors() {
    return directors;
  }

  public void setDirectors(List<Director> directors) {
    this.directors = directors;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }



  //----------------------------------------------------- METHODES --------------------------------------------------------------



}
