package com.francis.store.mappers;


import com.francis.store.dtos.RegisterUserRequest;
import com.francis.store.dtos.UpdateUserRequest;
import com.francis.store.dtos.UserDto;
import com.francis.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
     UserDto toDto(User user);
     User toEntity(RegisterUserRequest request);
     void update(UpdateUserRequest request, @MappingTarget User user );
}