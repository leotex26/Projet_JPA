package fr.diginamic;


import fr.diginamic.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  // LOGGER PRINCIPAL
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

  logger.info("Hello World!");



    AppService appService = new AppService();
    appService.executer("Truc");


  }
}