package fr.diginamic;

import fr.diginamic.config.AppConfig;
import fr.diginamic.model.Country;
import fr.diginamic.service.ImportService;
import fr.diginamic.util.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Classe exécutable injectant le contenu de fichiers CSV dans la base de données SQL
 */
public class AppLoadDataFromCSVIntoBDD {

  private static final Logger LOG = LoggerFactory.getLogger(AppLoadDataFromCSVIntoBDD.class);

  public static void main(String[] args) {
    LOG.info("Starting AppLoadDataFromCSVIntoBDD");

    // Création de l'EntityManagerFactory et de l'EntityManager
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("movies_db");
    EntityManager em = emf.createEntityManager();

    try {
      // Instanciation du service d'import avec l'EntityManager
      ImportService importService = new ImportService(em);

      // Lancement de l'import complet
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

