package com.v1.bms.project.persistence.mapper;

import com.v1.bms.project.domain.entity.CustomerAccountOpeningRequestEntity;
import com.v1.bms.project.persistence.entity.CustomerAccountOpeningRequestPersistenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerOpeningEntityMapper {

    CustomerOpeningEntityMapper INSTANCE = Mappers.getMapper(CustomerOpeningEntityMapper.class);

    CustomerAccountOpeningRequestPersistenceEntity toPersonalAccountOpeningRequestPersistenceEntity(CustomerAccountOpeningRequestEntity personalAccountOpeningRequestEntity);

    CustomerAccountOpeningRequestEntity toPersonalAccountOpeningRequestEntity(CustomerAccountOpeningRequestPersistenceEntity personalAccountOpeningRequestPersistenceEntity);

}
