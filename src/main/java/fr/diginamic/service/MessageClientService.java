package fr.diginamic.service;

import fr.diginamic.model.Actor;
import fr.diginamic.model.Film;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Service d'échange avec le client, adapté pour une interface graphique JavaFX
 */
public class MessageClientService {

  private final Logger LOG;
  private final Stage primaryStage;
  EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("movies_db");
  EntityManager em = entityManagerFactory.createEntityManager();
  private final ImportService importService = new ImportService(em);
  UIService ui = new UIService();

  public MessageClientService(Logger log, Stage stage) {
    this.LOG = log;
    this.primaryStage = stage;
  }

  /**
   * Crée un menu principal JavaFX avec boutons
   *
   * @return VBox contenant le menu
   */
  public VBox createMenu() {
    VBox menuBox = new VBox(10);
    menuBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

    Label title = new Label("Menu principal");
    title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

    Button btn1 = new Button("Afficher la filmographie d'un acteur");
    Button btn2 = new Button("Affichage du casting d’un film");
    Button btn3 = new Button("Affichage des films sortis entre deux années");
    Button btn4 = new Button("Affichage des films communs à deux acteurs/actrices");
    Button btn5 = new Button("Affichage des acteurs communs à deux films");
    Button btn6 = new Button("Affichage des films sortis entre deux années données et qui ont un acteur/actrice donné au casting");
    Button btn7 = new Button("07 - Fin de l’application");

    // Associer des actions à chaque bouton
    btn1.setOnAction(e -> handleChoice(1));
    btn2.setOnAction(e -> handleChoice(2));
    btn3.setOnAction(e -> handleChoice(3));
    btn4.setOnAction(e -> handleChoice(4));
    btn5.setOnAction(e -> handleChoice(5));
    btn6.setOnAction(e -> handleChoice(6));
    btn7.setOnAction(e -> handleChoice(7));

    menuBox.getChildren().addAll(title, btn1, btn2, btn3, btn4, btn5, btn6, btn7);

    return menuBox;
  }

  /**
   * Méthode qui sera appelée lors du clic sur un bouton
   */
  private void handleChoice(int choice) {
    switch (choice) {
      /** TOUT LES FILMS D'UN ACTEUR**/
      case 1 -> {
        LOG.info("Option 1 sélectionnée : Filmographie d'un acteur");

        TextField[] tfHolder = new TextField[1];

        VBox page = ui.makeBoxForInput(
          "Entrez le nom de votre acteur :",
          "Nom de l'acteur",
          () -> {
            String name = tfHolder[0].getText();
            LOG.info("Acteur entré : " + name);
            Actor actor = importService.actorService.findByName(name);
            if (actor == null) {
              primaryStage.getScene().setRoot(createMenu());
            }
            primaryStage.getScene().setRoot(ui.displayActorFilms(actor));
            primaryStage.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            ;

          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );

        // Récupération du TextField (petite astuce)
        tfHolder[0] = (TextField) page.getChildren().get(1);

        primaryStage.getScene().setRoot(page);
      }
      /** TOUT LES ACTEUR D'UN FILM **/
      case 2 -> {
        LOG.info("Option 2 sélectionnée : Casting d'un film");

        TextField[] tfHolder = new TextField[1];

        VBox page = ui.makeBoxForInput(
          "Entrez le nom de votre film :",
          "Nom du film",
          () -> {
            String name = tfHolder[0].getText();
            LOG.info("Film entré : " + name);
            Film film = importService.filmService.findByName(name);
            if (film == null) {
              primaryStage.getScene().setRoot(createMenu());
            }
            primaryStage.getScene().setRoot(ui.displayActorsOfFilm(film));
            primaryStage.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            ;

          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );

        // Récupération du TextField (petite astuce)
        tfHolder[0] = (TextField) page.getChildren().get(1);

        primaryStage.getScene().setRoot(page);

      }
      case 3 -> {
        LOG.info("Option 3 sélectionnée : Films entre deux années");

        TextField[] tfHolder = new TextField[3];

        VBox page = ui.makeBoxForInput(
          "Entrez une période :",
          "Année début",
          "Année fin",
          () -> {
            // Validation
            String startYearStr = tfHolder[0].getText();
            String endYearStr = tfHolder[1].getText();

            try {
              int yearStart = Integer.parseInt(startYearStr);
              int yearEnd = Integer.parseInt(endYearStr);

              // Récupération
              List<Film> films = importService.filmService.findAllByDate(yearStart, yearEnd);

              primaryStage.getScene().setRoot(ui.displayFilmsBetweenYears(yearStart, yearEnd, films));

            } catch (NumberFormatException ex) {
              LOG.error("Mauvais format pour les années !");
              primaryStage.getScene().setRoot(createMenu());
            }
          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );

        // récupération des textfields
        tfHolder[0] = (TextField) page.getChildren().get(1);
        tfHolder[1] = (TextField) page.getChildren().get(2);

        primaryStage.getScene().setRoot(page);
      }
      case 4 -> {
        LOG.info("Option 4 sélectionnée : Films communs à deux acteurs");
        TextField[] tfHolder = new TextField[3];

        VBox page = ui.makeBoxForInput(
          "Entrez vos deux acteurs :",
          "premier acteur",
          "deuxième acteur",
          () -> {
            // Validation
            String firstActorStr = tfHolder[0].getText();
            String secondActorStr = tfHolder[1].getText();
            // Récupération
            Actor firstActor = importService.actorService.findByName(firstActorStr);
            Actor secondActor = importService.actorService.findByName(secondActorStr);
            List<Film> films = importService.filmService.findAllByActor(firstActor, secondActor);

            primaryStage.getScene().setRoot(ui.displayFilmsCommunsBetweenTwoActors(firstActor, secondActor, films));

          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );

        // récupération des textfields
        tfHolder[0] = (TextField) page.getChildren().get(1);
        tfHolder[1] = (TextField) page.getChildren().get(2);

        primaryStage.getScene().setRoot(page);


      }
      case 5 -> {
        LOG.info("Option 5 sélectionnée : Acteurs communs à deux films");

        TextField[] tfHolder = new TextField[2];

        VBox page = ui.makeBoxForInput(
          "Entrez les titres des deux films :",
          "Titre film 1",
          "Titre film 2",
          () -> {
            String film1Title = tfHolder[0].getText();
            String film2Title = tfHolder[1].getText();

            LOG.info("Film 1: " + film1Title);
            LOG.info("Film 2: " + film2Title);

            if(film1Title.isBlank() || film2Title.isBlank()) {
              LOG.warn("Les deux titres doivent être renseignés");
              primaryStage.getScene().setRoot(createMenu());
              return;
            }

            Film film1 = importService.filmService.findByName(film1Title);
            Film film2 = importService.filmService.findByName(film2Title);

            LOG.info("Film 1 > titre : " + film1.getTitle());
            LOG.info("Film 2 > titre : " + film2.getTitle());

            if(film1 == null || film2 == null) {
              LOG.warn("Au moins un des films n'a pas été trouvé");
              primaryStage.getScene().setRoot(createMenu());
              return;
            }

            List<Actor> commonActors = importService.actorService.findCommonActorsBetweenFilms(film1, film2);

            primaryStage.getScene().setRoot(ui.displayActorsCommonBetweenTwoFilms(film1, film2, commonActors));
          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );

        tfHolder[0] = (TextField) page.getChildren().get(1);
        tfHolder[1] = (TextField) page.getChildren().get(2);

        primaryStage.getScene().setRoot(page);
      }
      case 6 -> {
        LOG.info("Option 6 sélectionnée : Films entre deux années avec un acteur donné");

        TextField[] tfHolder = new TextField[3];

        VBox page = ui.makeBoxForInput(
          "Entrez la période et le nom de l'acteur :",
          "Année début",
          "Année fin",
          "Nom acteur",
          () -> {
            String startYearStr = tfHolder[0].getText();
            String endYearStr = tfHolder[1].getText();
            String actorName = tfHolder[2].getText();

            if (startYearStr.isBlank() || endYearStr.isBlank() || actorName.isBlank()) {
              LOG.warn("Tous les champs doivent être renseignés");
              primaryStage.getScene().setRoot(createMenu());
              return;
            }

            try {
              int yearStart = Integer.parseInt(startYearStr);
              int yearEnd = Integer.parseInt(endYearStr);

              Actor actor = importService.actorService.findByName(actorName);
              if (actor == null) {
                LOG.warn("Acteur non trouvé");
                primaryStage.getScene().setRoot(createMenu());
                return;
              }

              List<Film> films = importService.filmService.findByDateAndActor(yearStart, yearEnd, actor);

              primaryStage.getScene().setRoot(ui.displayFilmsBetweenYearsAndActor(yearStart, yearEnd, actor, films));

            } catch (NumberFormatException ex) {
              LOG.error("Format d'année incorrect !");
              primaryStage.getScene().setRoot(createMenu());
            }
          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );

        tfHolder[0] = (TextField) page.getChildren().get(1);
        tfHolder[1] = (TextField) page.getChildren().get(2);
        tfHolder[2] = (TextField) page.getChildren().get(3);

        primaryStage.getScene().setRoot(page);
      }
      case 7 -> {
        LOG.info("Fin de l'application");
        System.exit(0);
      }
      default -> LOG.warn("Option inconnue");
    }
  }

}
