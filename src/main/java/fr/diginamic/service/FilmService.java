package fr.diginamic.service;

import fr.diginamic.model.*;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;

public class FilmService {

  private final EntityManager em;
  private final PlaceService placeService;
  private final GenreService genreService;
  private final LanguageService languageService;
  private final ActorService actorService;
  private final DirectorService directorService;
  private final CountryService countryService;

  public FilmService(EntityManager em,
                     PlaceService placeService,
                     GenreService genreService,
                     LanguageService languageService,
                     ActorService actorService,
                     DirectorService directorService,
                     CountryService countryService) {
    this.em = em;
    this.placeService = placeService;
    this.genreService = genreService;
    this.languageService = languageService;
    this.actorService = actorService;
    this.directorService = directorService;
    this.countryService = countryService;
  }

  /**
   * extrait les données des films depuis le fichier csv
   */
  public void extractAllFromCSV() {
    em.getTransaction().begin();

    List<String[]> results = CSVReader.readFromResources("files/films.csv", 3000);
    List<String[]> films_directors = CSVReader.readFromResources("files/film_realisateurs.csv", 3000);

    for (String[] row : results) {
      if (row.length < 10) continue;

      Film film = new Film();
      film.setImdbId(row[0]);
      film.setTitle(row[1]);
      film.setYearRaw(row[2]);

      // Parsing années
      try {
        String[] years = row[2].split("–");
        film.setYearStart(Integer.parseInt(years[0].trim()));
        film.setYearEnd(Integer.parseInt(years[0].trim()));
        if (years.length > 1) {
          film.setYearEnd(Integer.parseInt(years[1].trim()));
        }
      } catch (Exception ignored) {}

      try {
        film.setRating(Double.parseDouble(row[3].replace(",", ".")));
      } catch (Exception ignored) {}

      film.setUrl(row[4]);
      film.setSummary(row[8]);

      // Places
      if (row[5] != null && !row[5].isBlank()) {
        String[] placeNames = row[5].split(",");
        for (String name : placeNames) {
          Place place = new Place();
          place.setName(name.trim());
          film.getFilmingLocations().add(placeService.createIfNotExist(place));
        }
      }

      // Genres
      if (row[6] != null && !row[6].isBlank()) {
        String[] literalGenres = row[6].split(",");
        for (String g : literalGenres) {
          film.getGenres().add(genreService.createIfNotExist(g.trim()));
        }
      }

      // Languages
      if (row[7] != null && !row[7].isBlank()) {
        film.getLanguages().add(languageService.createIfNotExist(row[7].trim()));
      }

      // Country
      if (row[row.length-1] != null && !row[row.length-1].isBlank()) {
        String countryName = row[row.length-1].trim();
        Country existingCountry = countryService.find(countryName);
        if (existingCountry == null) {
          Country newCountry = new Country();
          newCountry.setName(countryName);
          existingCountry = countryService.create(newCountry);
        }
        film.setProducerCountry(existingCountry);
      }

      // Persister ou mettre à jour
      createOrUpdate(film);
    }

    for(String[] row : films_directors) {
      Film film = findByIMDB(row[0]);
      if (film == null) {continue;}
      Director director = directorService.findByIMDB(row[1]);
      if (director == null) {continue;}

      film.getDirectors().add(director);
      createOrUpdate(film);
    }


    em.getTransaction().commit();
  }





  /**
   * permet de trouver un film par son identifiant imdb
   * @param s
   * @return
   */
  public Film findByIMDB(String s) {
    if (s == null || s.isBlank()) return null;

    List<Film> existing = em.createQuery(
        "SELECT f FROM Film f WHERE f.imdbId = :imdbIdentifiant",
        Film.class)
      .setParameter("imdbIdentifiant", s)
      .getResultList();

    if (!existing.isEmpty()) return existing.get(0);

    return null;
  }


  /**
   *
   * @param film
   * @return
   */
  public Film createOrUpdate(Film film) {
    List<Film> existing = em.createQuery(
        "SELECT f FROM Film f WHERE f.imdbId = :imdbId", Film.class)
      .setParameter("imdbId", film.getImdbId())
      .getResultList();

    if (!existing.isEmpty()) {
      // Le film existe déjà → mise à jour
      Film existingFilm = existing.get(0);
      existingFilm.setTitle(film.getTitle());
      existingFilm.setYearStart(film.getYearStart());
      existingFilm.setYearEnd(film.getYearEnd());
      existingFilm.setYearRaw(film.getYearRaw());
      existingFilm.setRating(film.getRating());
      existingFilm.setSummary(film.getSummary());
      existingFilm.setUrl(film.getUrl());
      existingFilm.setProducerCountry(film.getProducerCountry());
      existingFilm.setGenres(film.getGenres());
      existingFilm.setLanguages(film.getLanguages());
      existingFilm.setDirectors(film.getDirectors());
      existingFilm.setFilmingLocations(film.getFilmingLocations());
      existingFilm.setRoles(film.getRoles());

      return em.merge(existingFilm);
    }

    // Sinon persister le nouveau film
    em.persist(film);
    return film;
  }



}
