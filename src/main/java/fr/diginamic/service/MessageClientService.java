package fr.diginamic.service;

import fr.diginamic.model.Actor;
import fr.diginamic.model.Film;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.util.List;
import java.util.Properties;


/**
 * Service d'échange avec le client, adapté pour une interface graphique JavaFX
 */
public class MessageClientService {

  private final Logger LOG;
  private final Stage primaryStage;
  EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("movies_db");
  EntityManager em = entityManagerFactory.createEntityManager();
  private final ImportService importService = new ImportService(em);
  Properties props = new Properties();


  public MessageClientService(Logger log, Stage stage) {
    this.LOG = log;
    this.primaryStage = stage;

    try {
      props.load(getClass().getResourceAsStream("/application.properties"));
    } catch (Exception e) {
      throw new RuntimeException("Impossible de charger application.properties", e);
    }

    String logoPath = props.getProperty("logo.path");
    Image icon = new Image(getClass().getResourceAsStream(logoPath));
    primaryStage.getIcons().add(icon);

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

    Button btn1 = new Button("filmographie d'un acteur");
    Button btn2 = new Button("casting d’un film");
    Button btn3 = new Button("films sortis entre deux années");
    Button btn4 = new Button("films communs à deux acteurs/actrices");
    Button btn5 = new Button("acteurs communs à deux films");
    Button btn6 = new Button("films sortis entre deux années données et qui ont un acteur/actrice donné au casting");
    Button btn7 = new Button("Fin de l’application");

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
    UIService ui = new UIService(primaryStage);


    switch (choice) {
      /** TOUT LES FILMS D'UN ACTEUR **/
      case 1 -> {
        LOG.info("Option 1 sélectionnée : Filmographie d'un acteur");

        TextField[] tfHolder = new TextField[1];

        VBox page = ui.makeBoxForInput(
          "Entrez le nom de votre acteur :",
          List.of("Nom de l'acteur"),
          () -> {
            String name = tfHolder[0].getText();
            LOG.info("Acteur entré : " + name);
            Actor actor = importService.actorService.findByName(name);
            if (actor == null) {
              showError("Acteur non trouvable");
              primaryStage.getScene().setRoot(createMenu());
            }
            VBox vb = ui.displayActorFilms(actor);

            SetTheBoxToTheScene(primaryStage, vb);
            applyCss();

          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );

        addExemple(page, "Robert Wagner, James Brown, Billy Boyd, Steve McQueen, Charles Bronson");

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
          List.of("Nom du film"),
          () -> {
            String name = tfHolder[0].getText();
            LOG.info("Film entré : " + name);
            Film film = importService.filmService.findByName(name);
            if (film == null) {
              showError("Film non trouvable");
              primaryStage.getScene().setRoot(createMenu());
            }
            VBox vb = ui.displayActorsOfFilm(film);
            SetTheBoxToTheScene(primaryStage, vb);
            applyCss();

          },
          () -> primaryStage.getScene().setRoot(createMenu())
        );
        addExemple(page, "La Tour infernale, Apocalypse Now, Le Parrain");

        // Récupération du TextField (petite astuce)
        tfHolder[0] = (TextField) page.getChildren().get(1);

        primaryStage.getScene().setRoot(page);

      }
      case 3 -> {
        LOG.info("Option 3 sélectionnée : Films entre deux années");

        TextField[] tfHolder = new TextField[3];

        VBox page = ui.makeBoxForInput(
          "Entrez une période :",
          List.of("Année début",
            "Année fin"),
          () -> {
            // Validation
            String startYearStr = tfHolder[0].getText();
            String endYearStr = tfHolder[1].getText();

            try {
              int yearStart = Integer.parseInt(startYearStr);
              int yearEnd = Integer.parseInt(endYearStr);

              // Récupération
              List<Film> films = importService.filmService.findAllByDate(yearStart, yearEnd);

              VBox vb = ui.displayFilmsBetweenYears(yearStart, yearEnd, films);
              SetTheBoxToTheScene(primaryStage, vb);
              applyCss();

            } catch (NumberFormatException ex) {
              LOG.error("Mauvais format pour les années !");
              showError("Il y a eut un problème inconnu, désolé pour le désagrément");
              primaryStage.getScene().setRoot(createMenu());
            }
          },
          () -> {
            primaryStage.getScene().setRoot(createMenu());
          }
        );
        addExemple(page, "1975 à 2000");

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
          List.of("premier acteur",
            "deuxième acteur"),
          () -> {
            // Validation
            String firstActorStr = tfHolder[0].getText();
            String secondActorStr = tfHolder[1].getText();

            // Récupération
            Actor firstActor = importService.actorService.findByName(firstActorStr);
            Actor secondActor = importService.actorService.findByName(secondActorStr);

            if (firstActor == null || secondActor == null) {
              LOG.error("acteurs non trouvés en base !");
              showError("Un des deux acteurs est introuvable");
              primaryStage.getScene().setRoot(createMenu());
            }

            List<Film> films = importService.filmService.findAllByActor(firstActor, secondActor);

            VBox vb = ui.displayFilmsCommunsBetweenTwoActors(firstActor, secondActor, films);
            SetTheBoxToTheScene(primaryStage, vb);
            applyCss();

          },
          () -> {
            primaryStage.getScene().setRoot(createMenu());
          }
        );
        // exemples
        addExemple(page, "Robert Wagner, James Brown, Billy Boyd, Steve McQueen, Charles Bronson");

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
          List.of("Titre film 1", "Titre film 2"),
          () -> {
            String film1Title = tfHolder[0].getText();
            String film2Title = tfHolder[1].getText();

            LOG.info("Film 1: " + film1Title);
            LOG.info("Film 2: " + film2Title);

            if (film1Title.isBlank() || film2Title.isBlank()) {
              LOG.warn("Les deux titres doivent être renseignés");
              primaryStage.getScene().setRoot(createMenu());
              return;
            }

            Film film1 = importService.filmService.findByName(film1Title);
            Film film2 = importService.filmService.findByName(film2Title);

            if (film1 == null || film2 == null) {
              LOG.warn("Au moins un des films n'a pas été trouvé");
              showError("un des films n'est pas trouvable");
              primaryStage.getScene().setRoot(createMenu());
              return;
            }

            LOG.info("Film 1 > titre : " + film1.getTitle());
            LOG.info("Film 2 > titre : " + film2.getTitle());

            List<Actor> commonActors = importService.actorService.findCommonActorsBetweenFilms(film1, film2);

            VBox vb = ui.displayActorsCommonBetweenTwoFilms(film1, film2, commonActors);
            SetTheBoxToTheScene(primaryStage, vb);
            applyCss();
          },
          () -> {
            primaryStage.getScene().setRoot(createMenu());
          }
        );
        // exemples
        addExemple(page, "La Tour infernale, Apocalypse Now, Le Parrain");

        tfHolder[0] = (TextField) page.getChildren().get(1);
        tfHolder[1] = (TextField) page.getChildren().get(2);

        primaryStage.getScene().setRoot(page);
      }
      case 6 -> {
        LOG.info("Option 6 sélectionnée : Films entre deux années avec un acteur donné");

        TextField[] tfHolder = new TextField[3];

        VBox page = ui.makeBoxForInput(
          "Entrez la période et le nom de l'acteur :",
          List.of("Année début", "Année fin", "Nom acteur"),
          () -> {
            String startYearStr = tfHolder[0].getText();
            String endYearStr = tfHolder[1].getText();
            String actorName = tfHolder[2].getText();

            if (startYearStr.isBlank() || endYearStr.isBlank() || actorName.isBlank()) {
              LOG.warn("Tous les champs doivent être renseignés");
              showError("années ou acteur non reconnus");
              primaryStage.getScene().setRoot(createMenu());
              return;
            }

            try {
              int yearStart = Integer.parseInt(startYearStr);
              int yearEnd = Integer.parseInt(endYearStr);

              Actor actor = importService.actorService.findByName(actorName);
              if (actor == null) {
                LOG.warn("Acteur non trouvé");
                showError("l'acteur entré n'est pas trouvable");
                primaryStage.getScene().setRoot(createMenu());
                return;
              }

              List<Film> films = importService.filmService.findByDateAndActor(yearStart, yearEnd, actor);

              VBox vb = ui.displayFilmsBetweenYearsAndActor(yearStart, yearEnd, actor, films);
              SetTheBoxToTheScene(primaryStage, vb);
              applyCss();

            } catch (NumberFormatException ex) {
              LOG.error("Format d'année incorrect !");
              primaryStage.getScene().setRoot(createMenu());
            }
          },
          () -> {
            primaryStage.getScene().setRoot(createMenu());
          }
        );
        // exemples
        addExemple(page, "Robert Wagner, James Brown, Billy Boyd, Steve McQueen, Charles Bronson");

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

  /**
   * applique le css qui est dans le fichier style.css dans les ressources
   */
  private void applyCss() {
    String cssPath = props.getProperty("style.path");
    primaryStage.getScene().getStylesheets().clear();
    primaryStage.getScene().getStylesheets()
      .add(getClass().getResource(cssPath).toExternalForm());
  }

  /**
   * set la box au Stage de la scene avec un bouton de retour au menu et une scrollbar
   *
   * @param primaryStage le Stage global
   * @param vb           la vbos a associer au stage
   */
  public void SetTheBoxToTheScene(Stage primaryStage, VBox vb) {
    Button backBtn = new Button("< Menu");
    backBtn.setOnAction(e -> primaryStage.getScene().setRoot(createMenu()));
    vb.getChildren().addAll(backBtn);

    // --- Ajout de la scrollbar (verticale) ---
    ScrollPane scrollPane = new ScrollPane(vb);
    scrollPane.setFitToWidth(true); // La VBox prend toute la largeur
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    primaryStage.getScene().setRoot(scrollPane);
  }

  /**
   * permet d'afficher une ligne d'exemple à saisir et de l'ajouter à la box
   *
   * @param page     vbox conteneur
   * @param exemples string d'exemples textuels
   */
  public void addExemple(VBox page, String exemples) {
    Label exempleActor = new Label("Exemple : " + exemples);
    exempleActor.setWrapText(true);
    exempleActor.setStyle("-fx-font-style: italic;");
    page.getChildren().addAll(exempleActor);
  }

  /**
   * affiche une popup qui affiche l'erreur à l'utilisateur
   *
   * @param message d'erreur
   */
  public void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Erreur");
    alert.setHeaderText(null); // pas de titre secondaire
    alert.setContentText(message);
    alert.showAndWait();
  }

}
