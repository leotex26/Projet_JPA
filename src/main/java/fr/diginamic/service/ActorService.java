package fr.diginamic.service;



import fr.diginamic.model.Actor;
import fr.diginamic.model.Film;
import fr.diginamic.model.Place;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;


/**
 * Classe de gestion de la logique métier qui permet d'importer les objets Acteurs depuis le csv
 * et qui permet d'echanger avec la bdd des objets Acteurs
 */
public class ActorService {



  private final EntityManager em;
  private final PlaceService placeService;
  private final PersonService personService;

  public ActorService(EntityManager em, PlaceService placeService,PersonService personService) {
    this.em = em;
    this.placeService = placeService;
    this.personService = personService;
  }

  public void extractAllFromCSV() {
    em.getTransaction().begin();
    HashSet<Actor> actors = new HashSet<>();

    List<String[]> results = CSVReader.readFromResources("files/acteurs.csv", 3000);

    for (String[] row : results) {
      Actor actor = new Actor();
      actor.setImdbId(row[0]);
      actor.setName(row[1]);
      actor.setBirthDate(row[2]);
      actor.setHeight(row[4]);
      actor.setUrl(row[5]);

      Place place = new Place();
      place.setName(row[3].trim());

      Place placeOfActor = placeService.createIfNotExist(place);
      actor.setBirthPlace(placeOfActor);
      actors.add(actor);
    }

    for (Actor actor : actors) personService.createIfNotExist(actor);
    em.getTransaction().commit();
  }

  public Actor findByIMDB(String s) {
    if (s == null || s.isBlank() ) return null;

    List<Actor> existing = em.createQuery(
        "SELECT a FROM Actor a WHERE a.imdbId = :imdbIdentifiant",
        Actor.class)
      .setParameter( "imdbIdentifiant", s )
      .getResultList();

    if (!existing.isEmpty()) return existing.get(0);

    return null;
  }
}
