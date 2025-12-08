package fr.diginamic.service;

import fr.diginamic.model.Actor;
import fr.diginamic.model.Film;
import fr.diginamic.model.Role;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

public class RoleService {

  private final EntityManager em;
  private final FilmService filmService;
  private final ActorService actorService;

  public RoleService(EntityManager em, FilmService filmService, ActorService actorService) {
    this.em = em;
    this.filmService = filmService;
    this.actorService = actorService;
  }



  /**
   * Crée un Role si aucun rôle existant n'a le même acteur pour le même film et personnage
   *
   * @param film Film concerné
   * @param actor Acteur qui joue le rôle
   * @param characterName Nom du personnage
   * @return le Role existant ou créé
   */
  public Role createIfNotExist(Film film, Actor actor, String characterName) {
    if (film == null || actor == null || characterName == null || characterName.isBlank()) {
      return null;
    }

    List<Role> existing = em.createQuery(
        "SELECT r FROM Role r WHERE r.film = :film AND r.actor = :actor AND r.characterName = :characterName",
        Role.class)
      .setParameter("film", film)
      .setParameter("actor", actor)
      .setParameter("characterName", characterName.trim())
      .getResultList();

    if (!existing.isEmpty()) return existing.get(0);

    Role role = new Role();
    role.setFilm(film);
    role.setActor(actor);
    role.setCharacterName(characterName.trim());

    em.persist(role);
    return role;
  }

  /**
   * implementation des roles dansla base
   */
  public void extractAllFromCSV() {
    em.getTransaction().begin();

    List<String[]> results = CSVReader.readFromResources("files/roles.csv", 3000);

    for (String[] row : results) {
      if (row.length < 3) {
        System.out.println("Ligne CSV incomplète : " + Arrays.toString(row));
        continue;
      }

      String filmImdb = row[0];
      String actorImdb = row[1];
      String characterName = row[2];

      Film film = filmService.findByIMDB(filmImdb);
      if (film == null) {
        System.out.println("Film non trouvé pour : " + filmImdb);
        continue;
      }

      Actor actor = actorService.findByIMDB(actorImdb);
      if (actor == null) {
        System.out.println("Acteur non trouvé pour : " + actorImdb);
        continue;
      }

      Role role = new Role();
      role.setFilm(film);
      role.setActor(actor);
      role.setCharacterName(characterName);

      em.persist(role);
    }

    em.getTransaction().commit();
  }
}

