package fr.diginamic.util;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de lire ligne par ligne un fichier CSV
 */
public class CSVReader {

  /**
   * @param path     chemin du fichier CSV
   * @param maxLines nombre maximum de lignes à lire (après l'en-tête)
   * @return Liste de lignes CSV
   */
  public static List<String[]> read(String path, int maxLines) {
    List<String[]> lines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line;
      boolean first = true;
      int count = 0; // compteur de lignes lues

      while ((line = br.readLine()) != null) {
        if (first) {
          first = false; // on ignore l'en-tête
          continue;
        }

        lines.add(line.split(";"));
        count++;

        if (count >= maxLines) {
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return lines;
  }


  public static List<String[]> readFromResources(String resourcePath, int maxLines) {
    List<String[]> lines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(
      new InputStreamReader(
        CSVReader.class.getClassLoader().getResourceAsStream(resourcePath)))) {

      String line;
      boolean first = true;
      int count = 0;

      while ((line = br.readLine()) != null) {
        if (first) { first = false; continue; }
        lines.add(line.split(";"));
        count++;
        if (count >= maxLines) break;
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    return lines;
  }


}

