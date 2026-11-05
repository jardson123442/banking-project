package com.v1.bms.project.persistence.mapper;

import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.persistence.entity.CustomerPersistenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    CustomerEntityMapper INSTANCE = Mappers.getMapper(CustomerEntityMapper.class);

    CustomerPersistenceEntity toPersonalAccountOpeningRequestPersistenceEntity(CustomerEntity personalAccountOpeningRequestEntity);

    CustomerEntity toPersonalAccountOpeningRequestEntity(CustomerPersistenceEntity personalAccountOpeningRequestPersistenceEntity);

}
