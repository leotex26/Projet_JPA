package fr.diginamic.service;

import fr.diginamic.model.Director;
import fr.diginamic.model.Place;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Classe de gestion de la logique métier qui permet d'importer les objets Director depuis le csv
 * et qui permet d'echanger avec la bdd des objets Director
 */
@Service
public class DirectorService {
  /**
   * EntityManager unique founit par la classe d'appel
   **/
  private final EntityManager em;
  /**
   * Nos besoins en matière de services
   **/
  private final PlaceService placeService;
  private final PersonService personService;

  /**
   * Constructeur
   **/
  public DirectorService(EntityManager em, PlaceService placeService, PersonService personService) {
    this.em = em;
    this.placeService = placeService;
    this.personService = personService;
  }

  /**
   * Permet d'extraire les données du fichier realisateurs.csv et d'implementer les entités Director en bdd
   */
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
   * Permet de trouver un Realisateur par son identifiant imdb
   * @param s identifiant IMDB
   * @return Le realisateur concerné
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
