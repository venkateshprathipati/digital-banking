package com.novalabs.digitalbanking.identity.mapper;

import com.novalabs.digitalbanking.identity.dto.RegistrationRequest;
import com.novalabs.digitalbanking.identity.dto.RegistrationResponse;
import com.novalabs.digitalbanking.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegistrationRequest request);

    RegistrationResponse toResponse(User user);
}
