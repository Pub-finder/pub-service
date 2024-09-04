package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.models.AdditionalInfo;
import com.pubfinder.pubfinder.models.Pub;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {

  Mapper INSTANCE = Mappers.getMapper(Mapper.class);

  PubDto entityToDto(Pub entity);

  AdditionalInfoDto entityToDto(AdditionalInfo additionalInfo);

  Pub dtoToEntity(PubDto dto);

  AdditionalInfo dtoToEntity(AdditionalInfoDto dto);
}