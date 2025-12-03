package fr.diginamic;

import fr.diginamic.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe executable injectant le contenu de fichiers CSV dans une base de donnée SQL
 */
public class AppLoadDataFromCSVIntoBDD {

  /** LOGGER de l'appLoadDataFromCSVIntoBDD **/
  private static final Logger LOG = LoggerFactory.getLogger(AppService.class);

  public static void main(String[] args) {
    LOG.info("Starting AppLoadDataFromCSVIntoBDD");


  }
}
