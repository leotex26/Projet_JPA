package fr.diginamic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité Actor correspondant à la meme table en base
 * PS : la majorité des attributs liés à l'acteur se trouve dans la classe mère Person dont l'identifiant.
 */
@Entity
public class Actor extends Person {
  public Actor() {}

  /**
   * la taille de l'acteur
   */
  @Column(name = "height", length = 6)
  private String height;

  /**
   * ses diffèrents roles
   */
  @OneToMany(mappedBy = "actor")
  private List<Role> roles = new ArrayList<>();


  //----------------------------------------------------- GETTER / SETTER --------------------------------------------------------

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }


  //----------------------------------------------------- METHODES --------------------------------------------------------------


}
