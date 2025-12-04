package fr.diginamic.service;

import fr.diginamic.model.Country;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CountryService {

  private final EntityManager em;

  public CountryService(EntityManager em) {
    this.em = em;
  }

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

  public Country find(String name) {
    if (name == null || name.isBlank()) return null;

    List<Country> countries = em.createQuery(
        "SELECT c FROM Country c WHERE c.name = :name", Country.class)
      .setParameter("name", name.trim())
      .getResultList();

    return countries.isEmpty() ? null : countries.get(0);
  }



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

  public Country create(Country country) {
    em.persist(country);
    return country;
  }



}
