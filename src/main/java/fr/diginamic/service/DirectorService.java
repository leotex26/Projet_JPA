package fr.diginamic.service;

//ID;IDENTITE;DATE NAISSANCE;LIEU NAISSANCE;URL;

import fr.diginamic.model.Actor;
import fr.diginamic.model.Director;
import fr.diginamic.model.Film;
import fr.diginamic.model.Place;
import fr.diginamic.repository.ActorRepository;
import fr.diginamic.repository.DirectorRepository;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Classe de gestion de la logique métier qui permet d'importer les objets Director depuis le csv
 * et qui permet d'echanger avec la bdd des objets Director
 */
@Service
public class DirectorService {

  private final EntityManager em;
  private final PlaceService placeService;
  private final PersonService personService ;

  public DirectorService(EntityManager em, PlaceService placeService, PersonService personService) {
    this.em = em;
    this.placeService = placeService;
    this.personService = personService;
  }


  public void extractAllFromCSV() {
    em.getTransaction().begin();
    HashSet<Director> directors = new HashSet<>();

    List<String[]> results = CSVReader.readFromResources("files/realisateurs.csv", 3000);

    for (String[] row : results) {
      Director director = new Director();
      director.setImdbId(row[0]);
      director.setName(row[1]);
      director.setBirthDate(row[2]);
      director.setUrl(row[4]);

      Place place = new Place();
      place.setName(row[3].trim());

      Place placeOfDirector = placeService.createIfNotExist(place);
      director.setBirthPlace(placeOfDirector);
      directors.add(director);
    }

    for (Director director : directors) personService.createIfNotExist(director);
    em.getTransaction().commit();
  }


  /**
   * permet de trouver un Realisateur par son identifiant imdb
   * @param s
   * @return
   */
  public Director findByIMDB(String s) {
    if (s == null || s.isBlank()) return null;

    List<Director> existing = em.createQuery(
        "SELECT d FROM Director d WHERE d.imdbId = :imdbIdentifiant",
        Director.class)
      .setParameter("imdbIdentifiant", s)
      .getResultList();

    if (!existing.isEmpty()) return existing.get(0);

    return null;
  }
}
