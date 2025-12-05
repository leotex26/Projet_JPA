package fr.diginamic.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class SceneLoader {

  private final Stage stage;

  public SceneLoader(Stage stage) {
    this.stage = stage;
  }

  /**
   * Charge un fichier FXML et l'affiche sur le Stage principal
   * @param fxmlPath chemin vers le FXML (ex : "/views/actor-common-film.fxml")
   */
  public void loadScene(String fxmlPath) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      Parent root = loader.load();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Charge un FXML et renvoie le contrôleur associé
   * @param fxmlPath chemin vers le FXML
   * @param <T> type du contrôleur
   * @return le contrôleur associé au FXML
   */
  public <T> T loadSceneAndGetController(String fxmlPath) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      Parent root = loader.load();
      stage.setScene(new Scene(root));
      stage.show();
      return loader.getController();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}


