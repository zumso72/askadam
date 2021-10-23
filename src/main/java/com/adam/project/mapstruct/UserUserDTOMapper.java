package com.adam.project.mapstruct;

import com.adam.project.model.User;
import com.adam.project.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserUserDTOMapper {
    User userDTOToUser(UserDTO userDTO);
}
