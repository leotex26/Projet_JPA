package fr.diginamic.service;


import fr.diginamic.AppLoadDataFromCSVIntoBDD;
import fr.diginamic.model.Actor;
import fr.diginamic.repository.ActorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorService {

  private static final Logger LOG = LoggerFactory.getLogger(AppLoadDataFromCSVIntoBDD.class);

  @Autowired
  private ActorRepository actorRepository;

  public Actor save(Actor actor) {
    return actorRepository.save(actor);
  }
}
