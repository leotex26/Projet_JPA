package fr.diginamic.service;

import fr.diginamic.model.Country;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;

import java.util.HashSet;
import java.util.List;

/**
 * Classe de gestion de la logique métier des Country (Pays)
 */
public class CountryService {

  /**
   * EntityManager unique founit par la classe d'appel
   **/
  private final EntityManager em;

  /**
   * Constructeur
   **/
  public CountryService(EntityManager em) {
    this.em = em;
  }

  /**
   * Permet d'extraire les données du fichier pays.csv et d'implementer les entités Country en bdd
   */
  public void extractAllFromCSV() {
    em.getTransaction().begin();

    HashSet<Country> countries = new HashSet<>();
    List<String[]> results = CSVReader.readFromResources("files/pays.csv", 2500);

    for (String[] row : results) {
      Country country = new Country();
      country.setName(row[0]);
      country.setUrl(row[1]);
      countries.add(country);
    }

    for (Country country : countries) em.persist(country);
    em.getTransaction().commit();
  }


  /**
   * Fonction qui permet de retrouver un pays par son nom
   *
   * @param name
   * @return la premiere entité Pays correspondante au nom
   */
  public Country find(String name) {
    if (name == null || name.isBlank()) return null;

    List<Country> countries = em.createQuery(
        "SELECT c FROM Country c WHERE c.name = :name", Country.class)
      .setParameter("name", name.trim())
      .getResultList();

    return countries.isEmpty() ? null : countries.get(0);
  }


  /**
   * Verifie si un pays existe et le créer si ce n'est pas le cas
   * @param name
   * @param url
   * @return le pays associé au nom et à l'url founit
   */
  public Country createIfNotExist(String name, String url) {
    if (name == null || name.isBlank()) return null;

    List<Country> existing = em.createQuery(
        "SELECT c FROM Country c WHERE c.name = :name", Country.class)
      .setParameter("name", name.trim())
      .getResultList();

    if (!existing.isEmpty()) {
      return existing.get(0);
    }

    Country country = new Country();
    country.setName(name.trim());
    country.setUrl(url);
    em.persist(country);
    return country;
  }

  /**
   * Persmet de persister un Pays en base
   *
   * @param country
   * @return le pays une fois persisté
   */
  public Country create(Country country) {
    em.persist(country);
    return country;
  }


}
