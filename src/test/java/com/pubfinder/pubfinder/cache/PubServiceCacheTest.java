package com.pubfinder.pubfinder.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.service.PubService;
import com.pubfinder.pubfinder.util.TestUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

@SpringBootTest(properties = {
    "spring.datasource.url=",
    "spring.jpa.database-platform=",
    "spring.jpa.hibernate.ddl-auto=none",
})
public class PubServiceCacheTest {

  @Autowired
  private PubService pubService;
  @MockBean
  private PubRepository pubRepository;
  @Autowired
  private CacheManager cacheManager;

  @Test
  public void testGetPubs_CacheHit() {
    double radius = 2.0;

    when(pubRepository.findPubsWithInRadius(40.712810, 74.006010, radius)).thenReturn(
        TestUtil.generateListOfMockPubs());
    when(pubRepository.findPubsWithInRadius(40.712811, 74.006011, radius)).thenReturn(
        TestUtil.generateListOfMockPubs());

    List<PubDto> response1 = pubService.getPubs(40.712810, 74.006010, radius);
    List<PubDto> response2 = pubService.getPubs(40.712811, 74.006011, radius);

    assertEquals(response1, response2);

    verify(pubRepository, times(1)).findPubsWithInRadius(40.712810, 74.006010, radius);
    verify(pubRepository, times(0)).findPubsWithInRadius(40.712811, 74.006011, radius);
  }

  @Test
  public void testGetPubs_CacheMiss() {
    double radius = 15.0;

    when(pubRepository.findPubsWithInRadius(40.712810, 74.006010, radius)).thenReturn(
        TestUtil.generateListOfMockPubs());
    when(pubRepository.findPubsWithInRadius(40.712811, 74.006011, radius)).thenReturn(
        TestUtil.generateListOfMockPubs());

    List<PubDto> response1 = pubService.getPubs(40.712810, 74.006010, radius);
    List<PubDto> response2 = pubService.getPubs(40.712811, 74.006011, radius);

    verify(pubRepository, times(1)).findPubsWithInRadius(40.712810, 74.006010, radius);
    verify(pubRepository, times(1)).findPubsWithInRadius(40.712811, 74.006011, radius);
  }

  @Test
  public void testGetPub_CacheHit() throws ResourceNotFoundException {
    Pub pub = TestUtil.generateMockPub();

    when(pubRepository.findById(pub.getId())).thenReturn(Optional.of(pub));

    PubDto result1 = pubService.getPub(pub.getId());
    PubDto result2 = pubService.getPub(pub.getId());

    assertEquals(result1, result2);

    verify(pubRepository, times(1)).findById(pub.getId());
  }

  @Test
  public void testSearchPubsByTerm_CacheHit() {
    PubDto bigBen = PubDto.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
    PubDto liffey = PubDto.builder().id(UUID.randomUUID()).name("The Liffey").build();
    List<Object[]> dbRs = List.of(new Object[]{bigBen.getId(), bigBen.getName()},
        new Object[]{liffey.getId(), liffey.getName()});

    when(pubRepository.findPubsByNameContaining("The")).thenReturn(dbRs);

    List<PubDto> result1 = pubService.searchPubsByTerm("The");
    List<PubDto> result2 = pubService.searchPubsByTerm("The");

    assertEquals(result1, result2);

    verify(pubRepository, times(1)).findPubsByNameContaining("The");
  }

  @Test
  public void testSearchPubsByTerm_CacheMissToSmall() {
    PubDto bigBen = PubDto.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
    PubDto liffey = PubDto.builder().id(UUID.randomUUID()).name("The Liffey").build();
    List<Object[]> dbRs = List.of(new Object[]{bigBen.getId(), bigBen.getName()},
        new Object[]{liffey.getId(), liffey.getName()});

    when(pubRepository.findPubsByNameContaining("T")).thenReturn(dbRs);

    List<PubDto> result1 = pubService.searchPubsByTerm("T");
    List<PubDto> result2 = pubService.searchPubsByTerm("T");

    assertEquals(result1, result2);

    verify(pubRepository, times(2)).findPubsByNameContaining("T");
  }

  @Test
  public void testSearchPubsByTerm_CacheMissToBig() {
    PubDto bigBen = PubDto.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
    List<Object[]> dbRs = Collections.singletonList(new Object[]{bigBen.getId(), bigBen.getName()});

    when(pubRepository.findPubsByNameContaining("The Big Ben ")).thenReturn(dbRs);

    List<PubDto> result1 = pubService.searchPubsByTerm("The Big Ben ");
    List<PubDto> result2 = pubService.searchPubsByTerm("The Big Ben ");

    assertEquals(result1, result2);

    verify(pubRepository, times(2)).findPubsByNameContaining("The Big Ben ");
  }

}
