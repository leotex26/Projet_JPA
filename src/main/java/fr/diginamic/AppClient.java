package fr.diginamic;

import fr.diginamic.service.MessageClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe executable permettant à un client d'obtenir diffèrentes informations contenues dans la base de données
 */
public class AppClient {

  private static final Logger LOG = LoggerFactory.getLogger(AppClient.class);
  public static boolean quit = false;

  private static final MessageClientService messageClientService =
    new MessageClientService(LOG);

  public static void main(String[] args) {

    // Charger les données (à désactiver une fois que ta BDD est prête)
    // AppLoadDataFromCSVIntoBDD.main(args);

    LOG.info("Starting AppClient");

    while (!quit) {

      Integer choice = messageClientService.mainMenu();

      if (choice == null) {
        System.out.println(" Choix invalide, réessayez.");
        continue;
      }

      switch (choice) {
        case 1 -> {
          System.out.println(" [1] Filmographie d'un acteur");
        }
        case 2 -> {
          System.out.println(" [2] Casting d’un film");
        }
        case 3 -> {
          System.out.println(" [3] Films sortis entre deux années");
        }
        case 4 -> {
          System.out.println(" [4] Films communs entre acteurs");
        }
        case 5 -> {
          System.out.println(" [5] Acteurs communs entre films");
        }
        case 6 -> {
          System.out.println(" [6] Films entre années + acteur donné");
        }
        case 7 -> {
          System.out.println(" Fin de l’application");
          quit = true;
        }
      }
    }
  }
}

