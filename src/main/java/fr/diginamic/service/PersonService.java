package fr.diginamic.service;

import fr.diginamic.model.Person;
import jakarta.persistence.EntityManager;

/**
 * gere la logique métier des entités Person
 */
public class PersonService {
  /**
   * EntityManager unique founit par la classe d'appel
   **/
  private final EntityManager em;

  public PersonService(EntityManager em) {
    this.em = em;
  }

  public Person findByImdbId(String imdbId) {
    try {
      return em.createQuery(
          "SELECT p FROM Person p WHERE p.imdbId = :id", Person.class)
        .setParameter("id", imdbId)
        .getResultStream()
        .findFirst()
        .orElse(null);
    } catch (Exception e) {
      return null;
    }
  }

  public Person createIfNotExist(Person p) {

    // 1. rechercher si déjà en base
    Person existing = findByImdbId(p.getImdbId());

    if (existing != null) {
      return existing;
    }

    // 2. sinon on persiste
    em.persist(p);
    return p;
  }


}
