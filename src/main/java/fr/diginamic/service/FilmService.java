package fr.diginamic.service;

import fr.diginamic.AppLoadDataFromCSVIntoBDD;
import fr.diginamic.model.Film;
import fr.diginamic.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {

  private static final Logger LOG = LoggerFactory.getLogger(AppLoadDataFromCSVIntoBDD.class);

  @Autowired
  private FilmRepository filmRepository;

  public Film save(Film film) {
    return filmRepository.save(film);
  }

  public List<Film> findAll() {
    return filmRepository.findAll();
  }

  public Film findByImdbId(String imdb) {
    return filmRepository.findByImdbId(imdb);
  }
}
