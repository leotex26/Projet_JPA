package fr.diginamic.service;

import fr.diginamic.model.Actor;
import fr.diginamic.model.Film;
import fr.diginamic.model.Role;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe permettant d'afficher la plupart des écrans
 */
public class UIService {

  private Stage primaryStage;

  public UIService(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public VBox makeBoxForInput(
    String labelText,
    List<String> textFieldPrompts,
    Runnable onValidate,
    Runnable onBack) {

    Label label = new Label(labelText);

    // Liste des textfields générés automatiquement
    List<TextField> fields = new ArrayList<>();

    for (String prompt : textFieldPrompts) {
      TextField tf = new TextField();
      tf.setPromptText(prompt);
      fields.add(tf);
    }

    Button validateBtn = new Button("Valider");
    Button backBtn = new Button("< Retour");
    backBtn.setStyle("-fx-background-color: #C4C4C4;");

    validateBtn.setOnAction(e -> onValidate.run());
    backBtn.setOnAction(e -> onBack.run());

    HBox buttons = new HBox(20, backBtn, validateBtn);
    buttons.setAlignment(Pos.CENTER);

    // Construction dynamique de la VBox
    VBox page = new VBox(15);
    page.getChildren().add(label);
    page.getChildren().addAll(fields); // ajoute tous les champs sans duplication
    page.getChildren().add(buttons);

    page.setAlignment(Pos.CENTER);
    page.setStyle("-fx-padding: 20;");

    return page;
  }


  /**
   * Voir tout les films d'un actor
   * @param actor
   * @return la vbox a setter au stage
   */
  public VBox displayActorFilms(Actor actor) {
    // Récupérer tous les films via les rôles
    HashSet<Film> films = new HashSet<>();
    actor.getRoles().forEach(role -> films.add(role.getFilm()));

    // Titre
    Label title = new Label("Voici les films de " + actor.getName() + " :");
    title.getStyleClass().add("title");

    // VBox pour la liste des films
    VBox filmsBox = new VBox();
    filmsBox.setSpacing(5); // un peu d'espace entre les films
    filmsBox.getStyleClass().add("card");

    for (Film film : films) {
      String s = film.getTitle() + " - " + film.getYearEnd().toString();
      Label filmLabel = new Label(s);
      filmLabel.getStyleClass().add("card-label");
      filmsBox.getChildren().add(filmLabel);

    }

    // VBox principal avec titre + liste des films
    VBox box = new VBox(10); // espace vertical de 10 px
    box.getChildren().addAll(title, filmsBox);

    return box;
  }

  /**
   * la box avec tout les acteurs du film passé en paramètre et leur role
   * @param film : film concerné
   * @return la vbox a setter au stage
   */
  public VBox displayActorsOfFilm(Film film) {

    // Récupération directe des rôles
    Set<Role> roles = film.getRoles();

    // Titre
    Label title = new Label("Acteurs du film : " + film.getTitle());
    title.getStyleClass().add("title");

    // Conteneur principal des cartes
    VBox actorsBox = new VBox(10);
    actorsBox.setAlignment(Pos.CENTER);

    for (Role role : roles) {

      // Création d'une carte stylée pour chaque acteur
      VBox card = new VBox(5);
      card.getStyleClass().add("card");

      Label actorName = new Label(role.getActor().getName());
      actorName.getStyleClass().add("card-label");

      Label character = new Label("Personnage : " + role.getCharacterName());
      character.getStyleClass().add("card-label");

      card.getChildren().addAll(actorName, character);
      actorsBox.getChildren().add(card);
    }

    // Conteneur global
    VBox box = new VBox(20, title, actorsBox);
    box.setAlignment(Pos.TOP_CENTER);

    return box;
  }

  /**
   * créer la box avec les deux dates min et max et la liste des films concernés
   * @param yearStart : année min
   * @param yearEnd : année max
   * @param films : liste des films a afficher
   * @return la vbox a setter au stage
   */
  public VBox displayFilmsBetweenYears(int yearStart, int yearEnd, List<Film> films) {

    // Titre
    Label title = new Label("Voici les films de " + yearStart + " - " + yearEnd);
    title.getStyleClass().add("title");

    // Conteneur principal des cartes
    VBox fBox = new VBox(10);
    fBox.setAlignment(Pos.CENTER);

    for (Film film : films) {
      String directorName = "Inconnu";
      if (!film.getDirectors().isEmpty()) {
        directorName = film.getDirectors().iterator().next().getName();
      }
      String s = film.getTitle() + " - " + directorName;
      Label filmLabel = new Label(s);
      filmLabel.getStyleClass().add("card-label");
      fBox.getChildren().add(filmLabel);
    }


    VBox box = new VBox(10);
    box.getChildren().addAll(title, fBox);

    return box;

  }

  /**
   * créer la box avec les deux acteurs concernés et les films qu'ils ont en commun
   * @param firstActor : un acteur
   * @param secondActor : un autre acteur
   * @param films : les films qu'ils ont en commun
   * @return la vbox a setter au stage
   */
  public VBox displayFilmsCommunsBetweenTwoActors(Actor firstActor, Actor secondActor, List<Film> films) {
    // Titre avec noms des acteurs
    Label title = new Label("Films communs entre " + firstActor.getName() + " et " + secondActor.getName());
    title.getStyleClass().add("title");

    VBox filmsBox = new VBox(10);
    filmsBox.setAlignment(Pos.CENTER);

    if (films == null || films.isEmpty()) {
      Label noFilms = new Label("Aucun film commun trouvé.");
      noFilms.getStyleClass().add("card-label");
      filmsBox.getChildren().add(noFilms);
    } else {
      for (Film film : films) {
        // Récupération du premier réalisateur (ou "Inconnu")
        String directorName = "Inconnu";
        if (!film.getDirectors().isEmpty()) {
          directorName = film.getDirectors().iterator().next().getName();
        }
        // Construction du label pour le film
        Label filmLabel = new Label(film.getTitle() + " - Réalisateur : " + directorName);
        filmLabel.getStyleClass().add("card-label");
        filmsBox.getChildren().add(filmLabel);
      }
    }

    VBox box = new VBox(15, title, filmsBox);
    box.setAlignment(Pos.TOP_CENTER);
    box.setStyle("-fx-padding: 20;");

    return box;
  }

  /**
   * créer la box avec les deux films concernés et les acteurs qu'ils ont en commun
   * @param film1 : un film
   * @param film2 : un film
   * @param commonActors : les acteurs que ces deux films ont en commun
   * @return la vbox a setter au stage
   */
  public VBox displayActorsCommonBetweenTwoFilms(Film film1, Film film2, List<Actor> commonActors) {
    // Titre principal
    Label title = new Label("Acteurs communs aux films : \"" + film1.getTitle() + "\" & \"" + film2.getTitle() + "\"");
    title.getStyleClass().add("title");

    VBox actorsBox = new VBox(10);
    actorsBox.setAlignment(Pos.CENTER);

    if (commonActors == null || commonActors.isEmpty()) {
      Label noActorsLabel = new Label("Aucun acteur commun trouvé.");
      noActorsLabel.getStyleClass().add("card-label");
      actorsBox.getChildren().add(noActorsLabel);
    } else {
      for (Actor actor : commonActors) {
        Label actorLabel = new Label(actor.getName());
        actorLabel.getStyleClass().add("card-label");
        actorsBox.getChildren().add(actorLabel);
      }
    }

    VBox root = new VBox(15, title, actorsBox);
    root.setAlignment(Pos.TOP_CENTER);
    root.setStyle("-fx-padding: 20;");

    return root;
  }


  /**
   * créer la box avec les films d'un acteur entre deux dates
   * @param yearStart : année min
   * @param yearEnd : année max
   * @param actor : un acteur
   * @param films  : liste des films a afficher
   * @return la vbox a setter au stage
   */
  public VBox displayFilmsBetweenYearsAndActor(int yearStart, int yearEnd, Actor actor, List<Film> films) {
    // Titre principal
    Label title = new Label("Films de " + yearStart + " à " + yearEnd + " avec " + actor.getName());
    title.getStyleClass().add("title");

    VBox filmsBox = new VBox(10);
    filmsBox.setAlignment(Pos.CENTER);

    if (films == null || films.isEmpty()) {
      Label noFilmsLabel = new Label("Aucun film trouvé pour cette période et cet acteur.");
      noFilmsLabel.getStyleClass().add("card-label");
      filmsBox.getChildren().add(noFilmsLabel);
    } else {
      for (Film film : films) {
        Label filmLabel = new Label(film.getTitle());
        filmLabel.getStyleClass().add("card-label");
        filmsBox.getChildren().add(filmLabel);
      }
    }

    VBox root = new VBox(15, title, filmsBox);
    root.setAlignment(Pos.TOP_CENTER);
    root.setStyle("-fx-padding: 20;");

    return root;
  }

}
