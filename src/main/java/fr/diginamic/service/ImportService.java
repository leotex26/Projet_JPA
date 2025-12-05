package fr.diginamic.service;

import jakarta.persistence.EntityManager;


/**
 * Classe qui centralise les import
 */
public class ImportService {

  /** On dispatche les differentes classes services en fonction des besoins **/
  private final EntityManager em;
  private final FilmService filmService;
  private final GenreService genreService;
  private final LanguageService languageService;
  private final ActorService actorService;
  private final DirectorService directorService;
  private final PlaceService placeService;
  private final CountryService countryService;
  private final RoleService roleService;
  private final PersonService personService;

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