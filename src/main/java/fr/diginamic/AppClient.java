package fr.diginamic;

import fr.diginamic.service.MessageClientService;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe executable permettant à un client d'obtenir diffèrentes informations contenues dans la base de données
 */
public class AppClient extends Application {

  private static final Logger LOG = LoggerFactory.getLogger(AppClient.class);

  /**
   * Méthode lancée par le main d'AppClient
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    // Page de chargement simple
    Label loadingLabel = new Label("Chargement de la base de données...");
    ProgressIndicator progressIndicator = new ProgressIndicator();
    VBox root = new VBox(10, loadingLabel, progressIndicator);
    root.setAlignment(Pos.CENTER);
    Scene loadingScene = new Scene(root, 400, 200);
    primaryStage.setScene(loadingScene);
    primaryStage.show();

    // Task pour remplir la base en arrière-plan
    Task<Void> loadDataTask = new Task<>() {
      @Override
      protected Void call() {
        AppLoadDataFromCSVIntoBDD.main(new String[]{});
        return null;
      }
    };

    /** L'APPLI S'EST LANCÉE CORRECTEMENT > AFFICHAGE D'UN MENU' **/
    loadDataTask.setOnSucceeded(e -> {
      // Quand la base est prête, afficher le menu principal
      try {
        MessageClientService messageClientService = new MessageClientService(LOG, primaryStage);
        VBox menu =  messageClientService.createMenu();


        Scene menuScene = new Scene(new StackPane(menu), 600, 400);
        primaryStage.setScene(menuScene);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    /** ça echoue > message d'erreur **/
    loadDataTask.setOnFailed(e -> {
      // En cas d'erreur
      Throwable ex = loadDataTask.getException();
      ex.printStackTrace();
      loadingLabel.setText("Erreur lors du chargement de la base !");
    });

    new Thread(loadDataTask).start();  // lancer le Task dans un nouveau thread
  }

  /** lancement de l'appli **/
  public static void main(String[] args) {
    launch(args);
  }
}



