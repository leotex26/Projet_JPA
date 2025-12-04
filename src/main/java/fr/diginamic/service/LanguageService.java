package fr.diginamic.service;

import fr.diginamic.model.Language;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * gere la logique métier des Languages
 */
public class LanguageService {

  private final EntityManager em;

  public LanguageService(EntityManager em) {
    this.em = em;
  }


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
