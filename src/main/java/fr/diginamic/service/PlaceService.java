package fr.diginamic.service;

import fr.diginamic.model.Country;
import fr.diginamic.model.Place;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * gere la logique métier des Places
 */
public class PlaceService {
  /** EntityManager unique founit par la classe d'appel **/
  private final EntityManager em;
  private final CountryService countryService;

  public PlaceService(EntityManager em, CountryService countryService) {
    this.em = em;
    this.countryService = countryService;
  }


  /**
   * Crée un Place si elle n'existe pas encore en base
   * @param place instance Place avec au minimum le nom
   * @return l'entité persistée ou existante
   */
  public Place createIfNotExist(Place place) {
    if (place == null || place.getName() == null || place.getName().isBlank()) {
      return null;
    }

    TypedQuery<Place> query = em.createQuery(
      "SELECT p FROM Place p WHERE p.name = :name", Place.class);
    query.setParameter("name", place.getName().trim());

    Place existing = query.getResultStream().findFirst().orElse(null);

    if (existing != null) return existing;

    /** J'associe la Place a un Country**/
    Country country = new Country();
    String[] placeInfos = place.getName().split(","); // le pays étant toujours a la fin du nom
    country.setName(placeInfos[placeInfos.length - 1].trim());
    country = countryService.createIfNotExist(country.getName(),"");
    place.setCountry(country);


    em.persist(place);
    return place;
  }
}
