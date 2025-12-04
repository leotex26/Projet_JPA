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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Objects;

/**
 * Classe mère Personne contenant la majorité des attributs de Director et Actor
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

  public Person() {
  }

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
  @Column(name = "birth_date")
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
  @Column(name = "url", length = 255)
  private String url;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(name, person.name) && Objects.equals(birthDate, person.birthDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, birthDate);
  }

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

  public void setBirthDate(String birthDate) {
    this.birthDate = parseBirthDate(birthDate);
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

  public static LocalDate parseBirthDate(String str) {
    if (str == null || str.isBlank()) return null;

    str = str.trim();

    // 1) Format dd/MM/yyyy
    try {
      DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      return LocalDate.parse(str, f);
    } catch (Exception ignored) {}

    // 2) Format "d MMMM yyyy" → ex : "7 September 1931"
    try {
      DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
      return LocalDate.parse(str, f);
    } catch (Exception ignored) {}

    // 3) Format "MMMM d yyyy" → ex : "September 7 1931"
    try {
      DateTimeFormatter f = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
      return LocalDate.parse(str, f);
    } catch (Exception ignored) {}

    // 4) Format "MMMM yyyy" → jour manquant
    try {
      DateTimeFormatter f = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("MMMM yyyy")
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter(Locale.ENGLISH);
      return LocalDate.parse(str, f);
    } catch (Exception ignored) {}

    // 5) Format "yyyy" → mois et jour manquants
    try {
      DateTimeFormatter f = new DateTimeFormatterBuilder()
        .appendPattern("yyyy")
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter();
      return LocalDate.parse(str, f);
    } catch (Exception ignored) {}

    // Aucun format reconnu → retour null (évite crash)
    System.err.println("⚠ Date non reconnue : " + str);
    return null;
  }





}
