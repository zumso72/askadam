package com.adam.project.mapstruct;

import com.adam.project.model.User;
import com.adam.project.model.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProfileDTOUserMapper {
    User profileDTOToUser(ProfileDTO profileDTO);
    ProfileDTO userToProfileDTO(User user);
}
