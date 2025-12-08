package fr.diginamic.service;

import fr.diginamic.model.Actor;
import fr.diginamic.model.Film;
import fr.diginamic.model.Place;
import fr.diginamic.model.Role;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Classe de gestion de la logique métier qui permet d'importer les objets Acteurs depuis le csv
 * et qui permet d'echanger avec la bdd des objets Acteurs
 */
public class ActorService {


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
  public ActorService(EntityManager em, PlaceService placeService, PersonService personService) {
    this.em = em;
    this.placeService = placeService;
    this.personService = personService;
  }

  /**
   * Permet d'extraire les données du fichier acteurs.csv et d'implementer les entités Actor en bdd
   */
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


  /**
   * @param s identifiant IMDB de l'Actor
   * @return le premier résultat trouvé
   */
  public Actor findByIMDB(String s) {
    if (s == null || s.isBlank()) return null;

    List<Actor> existing = em.createQuery(
        "SELECT a FROM Actor a WHERE a.imdbId = :imdbIdentifiant",
        Actor.class)
      .setParameter("imdbIdentifiant", s)
      .getResultList();

    if (!existing.isEmpty()) return existing.get(0);

    return null;
  }

  /**
   * Trouve acteur par nom
   *
   * @param name
   * @return ateur
   */
  public Actor findByName(String name) {
    name = name.trim().toLowerCase();

    TypedQuery<Actor> query = em.createQuery(
      "SELECT a FROM Actor a WHERE a.name = :name", Actor.class);
    query.setParameter("name", name);
    List<Actor> results = query.getResultList();
    if (results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  /**
   * trouve tout les acteur communs à deux films
   *
   * @param film1
   * @param film2
   * @return la liste des acteurs
   */
  public List<Actor> findCommonActorsBetweenFilms(Film film1, Film film2) {
    Set<Role> rolesFilm1 = film1.getRoles();
    Set<Role> rolesFilm2 = film2.getRoles();

    List<Actor> commonActors = new ArrayList<>();

    for (Role role : rolesFilm1) {
      for (Role role2 : rolesFilm2) {
        if (role.getActor().getName().trim().equalsIgnoreCase(role2.getActor().getName().trim())) {
          commonActors.add(role.getActor());
        }
      }
    }

    return commonActors;

  }
}
