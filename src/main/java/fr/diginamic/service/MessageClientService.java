package fr.diginamic.service;

import org.slf4j.Logger;

import java.util.Scanner;

/**
 * réuni les diffèrentes méthodes d'échange avec le client
 */
public class MessageClientService {

  public MessageClientService(Logger log) {
  }

  public Integer mainMenu() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("01 - Afficher la filmographie d'un acteur donné");
    System.out.println("02 - Affichage du casting d’un film donné ");
    System.out.println("03 - Affichage des films sortis entre deux années données ");
    System.out.println("04 - Affichage des films communs à deux acteurs/actrices donnés. ");
    System.out.println("05 - Affichage des acteurs communs à deux films donnés");
    System.out.println("06 - Affichage des films sortis entre deux années données et qui ont un acteur/actrice donné au \n" +
      "casting");
    System.out.println("07 - Fin de l’application \n");

    String input = scanner.nextLine();
    return verifMainMenu(input);
  }

  public Integer verifMainMenu(String str) {

    if(str.toLowerCase().contains(" quit")) {return 7;}

    String s = str.trim().toLowerCase();

    // 1
    if (s.contains("1") || (s.contains("filmographie") && s.contains("acteur"))) {
      return 1;
    }

    // 2
    if (s.contains("2") || s.contains("casting")) {
      return 2;
    }

    // 6
    if (s.contains("6")
      || (s.contains("films sorti")
      && (s.contains("annee") || s.contains("année"))
      && (s.contains("act")) )) {
      return 6;
    }

    // 3
    if (s.contains("3")
      || (s.contains("films sorti")
      && (s.contains("annee") || s.contains("année")))) {
      return 3;
    }

    // 4
    if (s.contains("4")
      || (s.contains("films commun"))) {
      return 4;
    }

    // 5
    if (s.contains("5")
      || (s.contains("acteurs commun"))) {
      return 5;
    }


    // 7
    if (s.contains("7")
      || (s.contains("fin"))) {
      return 7;
    }

    return null;
  }


}
