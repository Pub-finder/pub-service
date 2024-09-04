package com.pubfinder.pubfinder.util;

import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.models.Accessibility;
import com.pubfinder.pubfinder.models.AdditionalInfo;
import com.pubfinder.pubfinder.models.OpeningHours;
import com.pubfinder.pubfinder.models.Pub;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestUtil {

  public static Map<DayOfWeek, List<OpeningHours>> generateMockOpeningHours() {
    Map<DayOfWeek, List<OpeningHours>> openingHours = new HashMap<>();

    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(18, 0);

    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      List<OpeningHours> hours = List.of(new OpeningHours(startTime, endTime));
      openingHours.put(dayOfWeek, hours);
    }

    return openingHours;
  }

  public static Pub generateMockPub() {
    return Pub.builder()
        .id(UUID.randomUUID())
        .name("name")
        .lat(1.0)
        .lng(1.0)
        .openingHours(generateMockOpeningHours())
        .location("location")
        .description("description")
        .price("$")
        .additionalInfo(generateMockAdditionalInfo())
        .build();
  }

  public static PubDto generateMockPubDTO() {
    return PubDto.builder()
        .name("name")
        .lat(1.0)
        .lng(1.0)
        .openingHours(generateMockOpeningHours())
        .location("location")
        .description("description")
        .price("$")
        .additionalInfoDto(generateMockAdditionalInfoDto())
        .build();
  }

  public static List<Pub> generateListOfMockPubs() {
    Pub pub1 = generateMockPub();
    pub1.setId(UUID.randomUUID());
    pub1.setLat(40.7128);
    pub1.setLng(74.0060);

    Pub pub2 = generateMockPub();
    pub2.setId(UUID.randomUUID());
    pub2.setLat(40.7130);
    pub2.setLng(74.0064);

    Pub pub3 = generateMockPub();
    pub3.setId(UUID.randomUUID());
    pub3.setLat(40.7132);
    pub3.setLng(74.0061);

    return List.of(pub1, pub2, pub3);
  }

  public static Accessibility generateMockAccessibility() {
    return Accessibility.builder()
        .accessibleEntrance(true)
        .accessibleSeating(true)
        .accessibleParking(false)
        .build();
  }

  public static AdditionalInfo generateMockAdditionalInfo() {
    return AdditionalInfo.builder()
        .outDoorSeating(true)
        .washroom(true)
        .website("website.com")
        .accessibility(
            Accessibility.builder()
                .accessibleEntrance(true)
                .accessibleParking(true)
                .accessibleSeating(true)
                .build()
        )
        .build();
  }

  public static AdditionalInfoDto generateMockAdditionalInfoDto() {
    return AdditionalInfoDto.builder()
        .outDoorSeating(true)
        .washroom(true)
        .website("website.com")
        .accessibility(
            Accessibility.builder()
                .accessibleEntrance(true)
                .accessibleParking(true)
                .accessibleSeating(true)
                .build()
        )
        .build();
  }
}
