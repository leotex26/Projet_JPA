package fr.diginamic.service;

import fr.diginamic.model.Actor;
import fr.diginamic.model.Film;
import fr.diginamic.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * classe de test d'actorService
 */
class ActorServiceTest {

  private EntityManager em;
  private PlaceService placeService;
  private PersonService personService;
  private ActorService actorService;

  @BeforeEach
  void setUp() {
    em = mock(EntityManager.class);
    placeService = mock(PlaceService.class);
    personService = mock(PersonService.class);

    actorService = new ActorService(em, placeService, personService);
  }

  /**
   * test de la methode FindByIMDB dans actorService
   */
  @Test
  void testFindByIMDB() {
    Actor actor = new Actor();
    actor.setImdbId("tt123");
    actor.setName("Gilbert Truc");

    TypedQuery<Actor> query = mock(TypedQuery.class); // on créer une query mocké
    // on simule son comportement
    when(em.createQuery(anyString(), eq(Actor.class))).thenReturn(query);
    when(query.setParameter(eq("imdbIdentifiant"), eq("tt123"))).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(actor));

    Actor result = actorService.findByIMDB("tt123");
    assertNotNull(result);
    assertEquals("Gilbert Truc", result.getName());
  }

  /**
   * test de la methode FindByName dans actorService
   */
  @Test
  void testFindByName() {
    Actor actor = new Actor();
    actor.setImdbId("tt123");
    actor.setName("Gilbert Truc");

    TypedQuery<Actor> query = mock(TypedQuery.class);

    when(em.createQuery(anyString(), eq(Actor.class))).thenReturn(query);
    when(query.setParameter(eq("name"), eq("Gilbert Truc"))).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(actor));

    Actor result = actorService.findByName("Gilbert Truc");
    assertNotNull(result);
    assertEquals("Gilbert Truc", result.getName());
  }

  /**
   * test de la methode findCommonActorsBetweenFilms dans actorService
   */
  @Test
  void testFindCommonActorsBetweenFilms() {
    Actor actor = new Actor();
    actor.setImdbId("tt123");
    actor.setName("Gilbert Truc");

    Actor actor1 = new Actor();
    actor1.setImdbId("tt315");
    actor1.setName("Françoise Gattel");

    Film film = new Film();
    film.setTitle("le magicien Noz");
    Film film1 = new Film();
    film1.setTitle("pouet");

    Role role = new Role();
    role.setCharacterName("lala");
    role.setActor(actor);
    role.setFilm(film);
    Role role1 = new Role();
    role1.setCharacterName("Po");
    role1.setActor(actor1);
    role1.setFilm(film1);
    Role role2 = new Role();
    role2.setCharacterName("Jason Vorhees");
    role2.setActor(actor);
    role2.setFilm(film1);

    actor.addRole(role);
    actor1.addRole(role1);
    actor.addRole(role2);

    film.addRole(role);
    film1.addRole(role1);
    film1.addRole(role2);

    // Film1 > role1 > actor1 ; role2 > actor
    // Film > role > actor
    List<Actor> results = actorService.findCommonActorsBetweenFilms(film,film1);
    assertEquals(1, results.size());
    assertEquals("Gilbert Truc", results.getFirst().getName());
  }


}
