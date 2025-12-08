package fr.diginamic.service;

import fr.diginamic.model.Language;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * gere la logique métier des Languages
 */
public class LanguageService {

  /** EntityManager unique founit par la classe d'appel **/
  private final EntityManager em;

  public LanguageService(EntityManager em) {
    this.em = em;
  }

  /**
   * persist un language en base si elle n'y est pas déjà
   * @return l'instance de Language concerné
   */
  public Language createIfNotExist(String name) {
    if (name == null || name.isBlank()) return null;

    List<Language> existing = em.createQuery(
        "SELECT l FROM Language l WHERE l.name = :name", Language.class)
      .setParameter("name", name.trim())
      .getResultList();

    if (!existing.isEmpty()) return existing.get(0);

    Language language = new Language();
    language.setName(name.trim());
    em.persist(language);
    return language;
  }
}
