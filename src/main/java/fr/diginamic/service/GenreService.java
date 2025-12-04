package fr.diginamic.service;

import fr.diginamic.model.Genre;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * gere la logique métier des Genres
 */
public class GenreService {


  private final EntityManager em;

  public GenreService(EntityManager em) {
    this.em = em;
  }



  public Genre createIfNotExist(String name) {
    if (name == null || name.isBlank()) return null;

    List<Genre> existing = em.createQuery(
        "SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
      .setParameter("name", name.trim())
      .getResultList();

    if (!existing.isEmpty()) {
      return existing.get(0);
    }

    Genre genre = new Genre();
    genre.setName(name.trim());
    em.persist(genre);
    return genre;
  }



  /**
   * implemente la liste des Genres depuis films.csv
   */
  public void extractAllFromCSV() {
    Set<Genre> genres = new HashSet<>();
    List<String[]> results = CSVReader.readFromResources("files/films.csv", 200);

    for (String[] row : results) {
      if (row.length < 7) continue;

      String[] literalGenres = row[6].split(",");
      for (String literalGenre : literalGenres) {
        Genre genre = new Genre();
        genre.setName(literalGenre.trim());
        genres.add(genre);
      }
    }

    em.getTransaction().begin();
    for (Genre genre : genres) em.persist(genre);
    em.getTransaction().commit();
  }
}
