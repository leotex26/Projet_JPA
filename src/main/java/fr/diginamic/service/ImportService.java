package fr.diginamic.service;

import jakarta.persistence.EntityManager;


/**
 * Classe qui centralise les import
 */
public class ImportService {

  /** On dispatche les differentes classes services en fonction des besoins **/
  public final EntityManager em;
  public final FilmService filmService;
  public final GenreService genreService;
  public final LanguageService languageService;
  public final ActorService actorService;
  public final DirectorService directorService;
  public final PlaceService placeService;
  public final CountryService countryService;
  public final RoleService roleService;
  public final PersonService personService;

  public ImportService(EntityManager em) {
    this.em = em;
    this.countryService = new CountryService(em);
    this.placeService = new PlaceService(em, countryService);
    this.personService = new PersonService(em);
    this.actorService = new ActorService(em, placeService, personService);
    this.directorService = new DirectorService(em, placeService, personService);
    this.genreService = new GenreService(em);
    this.languageService = new LanguageService(em);
    this.filmService = new FilmService(em, placeService, genreService, languageService, actorService, directorService, countryService);
    this.roleService = new RoleService(em, filmService ,actorService);
  }

  /** appel des differentes methodes d'import **/
  public void importAll() {
    countryService.extractAllFromCSV();
    genreService.extractAllFromCSV();
    actorService.extractAllFromCSV();
    directorService.extractAllFromCSV();
    filmService.extractAllFromCSV();
    roleService.extractAllFromCSV();
  }
}