package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * The type Pubs service.
 */
@Service
public class PubService {

  @Autowired
  private PubRepository pubRepository;

  /**
   * Gets pubs within the radius of the location(lat, lng).
   *
   * @param lat    the latitude
   * @param lng    the longitude
   * @param radius the radius
   * @return the pubs
   */
  @Cacheable(value = "getPubs",
      condition = "#radius <= 10",
      key = "#lat.toString().substring(0, 5) + '-' + "
          + "#lng.toString().substring(0, 5) + '-' + "
          + "#radius.toString()")
  public List<PubDto> getPubs(Double lat, Double lng, Double radius) {
    return pubRepository.findPubsWithInRadius(lat, lng, radius).stream()
        .map(Mapper.INSTANCE::entityToDto).toList();
  }

  /**
   * Gets pub.
   *
   * @param id the pub id
   * @return the pub
   * @throws ResourceNotFoundException the resource not found exception
   */
  @Cacheable(value = "getPub")
  public PubDto getPub(UUID id) throws ResourceNotFoundException {
    Pub pub = pubRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pub with id " + id + " was not found"));

    return Mapper.INSTANCE.entityToDto(pub);
  }

  /**
   * Search pubs by term list.
   *
   * @param term the term
   * @return list of pubs
   */
  @Cacheable(value = "getPubsByTerm",
      condition = "#term.length() > 1 && #term.length() < 9",
      key = "#term"
  )
  public List<PubDto> searchPubsByTerm(String term) {
    List<Object[]> pubs = pubRepository.findPubsByNameContaining(term);
    List<PubDto> pubsList = new ArrayList<>();
    pubs.forEach(
        (pub) -> pubsList.add(PubDto.builder().id((UUID) pub[0]).name((String) pub[1]).build()));

    return pubsList;
  }

  /**
   * Save pub.
   *
   * @param pub the pub
   * @return the pub dto
   * @throws BadRequestException the pub param is empty
   */
  public PubDto save(Pub pub) throws BadRequestException {
    if (pub == null) {
      throw new BadRequestException();
    }
    Pub savedPub = pubRepository.save(pub);
    return Mapper.INSTANCE.entityToDto(savedPub);
  }

  /**
   * Edit pub.
   *
   * @param pub the pub
   * @return the pub dto
   * @throws ResourceNotFoundException the resource not found exception
   * @throws BadRequestException       the pub param is empty
   */
  public PubDto edit(Pub pub) throws ResourceNotFoundException, BadRequestException {
    if (pub == null || pub.getId() == null) {
      throw new BadRequestException();
    }

    pubRepository.findById(pub.getId()).orElseThrow(
        () -> new ResourceNotFoundException("Pub with id " + pub.getId() + " was not found"));

    Pub savedPub = pubRepository.save(pub);
    return Mapper.INSTANCE.entityToDto(savedPub);
  }

  /**
   * Delete.
   *
   * @param pub the pub
   * @throws BadRequestException the pub param is empty
   */
  // TODO: change to taking pub id instead of the entire object
  public void delete(Pub pub) throws BadRequestException {
    if (pub == null) {
      throw new BadRequestException();
    }
    pubRepository.delete(pub);
  }

  /**
   * Gets additional info for the pub.
   *
   * @param id the id
   * @return the additional info
   * @throws ResourceNotFoundException the resource not found exception
   */
  public AdditionalInfoDto getAdditionalInfo(UUID id) throws ResourceNotFoundException {
    return pubRepository.findAdditionalInfoForPub(id)
        .map(Mapper.INSTANCE::entityToDto)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                "Additional Info for pub with id " + id + " was not found"));
  }

  public List<PubDto> savePubs(List<Pub> pubs) {
    List<Pub> savedPubs = pubRepository.saveAll(pubs);
    return savedPubs.stream().map(Mapper.INSTANCE::entityToDto).toList();
  }
}
