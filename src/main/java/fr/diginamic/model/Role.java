package fr.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;


/**
 * Entité Role correspondant à la meme table en base
 */
@Entity
public class Role {

  /**
   * Identifiant
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * libelle / nom du personnage
   */
  @Column(name = "character_name")
  private String characterName;

  /**
   * Film dans le lequel le personnage apparait
   */
  @ManyToOne
  @JoinColumn(name = "film_id")
  private Film film;

  /**
   * Acteur qui interprete le role
   */
  @ManyToOne
  @JoinColumn(name = "actor_id")
  private Actor actor;


  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCharacterName() {
    return characterName;
  }

  public void setCharacterName(String characterName) {
    this.characterName = characterName;
  }

  public Film getFilm() {
    return film;
  }

  public void setFilm(Film film) {
    this.film = film;
  }

  public Actor getActor() {
    return actor;
  }

  public void setActor(Actor actor) {
    this.actor = actor;
  }

  @Override
  public String toString() {
    return "Role{" +
      "id=" + id +
      ", characterName='" + characterName + '\'' +
      ", film=" + film.getTitle() +
      ", actor=" + actor.getName() +
      '}';
  }

  //----------------------------------------------------- METHODES --------------------------------------------------------------


}
