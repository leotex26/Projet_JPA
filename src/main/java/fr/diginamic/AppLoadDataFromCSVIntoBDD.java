package fr.diginamic;

import fr.diginamic.service.ImportService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Classe exécutable injectant le contenu de fichiers CSV dans la base de données SQL
 */
public class AppLoadDataFromCSVIntoBDD {

  /**
   * logger de la partie import csv
   **/
  private static final Logger LOG = LoggerFactory.getLogger(AppLoadDataFromCSVIntoBDD.class);

  /**
   * thread principal
   **/
  public static void main(String[] args) {
    LOG.info("Starting AppLoadDataFromCSVIntoBDD");

    /** Création de l'EntityManagerFactory et de l'EntityManager qui servira à toutes les classes services **/
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("movies_db");
    EntityManager em = emf.createEntityManager();

    try {
      ImportService importService = new ImportService(em);

      /**  Lancement de l'import complet des fichiers csv - leur implementation dans la bdd se fera dans les services **/
      importService.importAll();

    } catch (Exception e) {
      LOG.error("Erreur lors de l'import des CSV : ", e);
    } finally {
      // Fermeture de l'EntityManager et de l'EntityManagerFactory
      if (em != null && em.isOpen()) em.close();
      if (emf != null && emf.isOpen()) emf.close();
    }

    LOG.info("Import terminé");
  }
}

