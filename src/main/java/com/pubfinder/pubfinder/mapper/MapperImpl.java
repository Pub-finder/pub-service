package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.models.AdditionalInfo;
import com.pubfinder.pubfinder.models.Pub;

public class MapperImpl implements Mapper {

  @Override
  public PubDto entityToDto(Pub entity) {
    if (entity == null) {
      return null;
    }
    return PubDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .lat(entity.getLat())
        .lng(entity.getLng())
        .openingHours(entity.getOpeningHours())
        .location(entity.getLocation())
        .description(entity.getDescription())
        .additionalInfoDto(entityToDto(entity.getAdditionalInfo()))
        .rating(entity.getAvgRating())
        .volume(entity.getAvgVolume())
        .serviceRating(entity.getAvgServiceRating())
        .toiletRating(entity.getAvgToiletRating())
        .price(entity.getPrice())
        .build();
  }

  @Override
  public AdditionalInfoDto entityToDto(AdditionalInfo additionalInfo) {
    return AdditionalInfoDto.builder()
        .website(additionalInfo.getWebsite())
        .accessibility(additionalInfo.getAccessibility())
        .washroom(additionalInfo.getWashroom())
        .outDoorSeating(additionalInfo.getOutDoorSeating())
        .build();
  }

  @Override
  public Pub dtoToEntity(PubDto dto) {
    if (dto == null) {
      return null;
    }

    Pub.PubBuilder builder = Pub.builder()
        .name(dto.getName())
        .lat(dto.getLat())
        .lng(dto.getLng())
        .openingHours(dto.getOpeningHours())
        .location(dto.getLocation())
        .description(dto.getDescription())
        .additionalInfo(dtoToEntity(dto.getAdditionalInfoDto()))
        .price(dto.getPrice());

    if (dto.getId() != null) {
      builder.id(dto.getId());
    }

    return builder.build();
  }

  @Override
  public AdditionalInfo dtoToEntity(AdditionalInfoDto dto) {
    return AdditionalInfo.builder()
        .accessibility(dto.getAccessibility())
        .website(dto.getWebsite())
        .washroom(dto.getWashroom())
        .outDoorSeating(dto.getOutDoorSeating())
        .build();
  }
}
