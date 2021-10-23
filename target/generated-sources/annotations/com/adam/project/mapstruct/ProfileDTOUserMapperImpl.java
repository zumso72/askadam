package com.adam.project.mapstruct;

import com.adam.project.model.User;
import com.adam.project.model.dto.ProfileDTO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-23T16:46:32+0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class ProfileDTOUserMapperImpl implements ProfileDTOUserMapper {

    @Override
    public User profileDTOToUser(ProfileDTO profileDTO) {
        if ( profileDTO == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( profileDTO.getFirstName() );
        user.setLastName( profileDTO.getLastName() );

        return user;
    }

    @Override
    public ProfileDTO userToProfileDTO(User user) {
        if ( user == null ) {
            return null;
        }

        String firstName = null;
        String lastName = null;

        firstName = user.getFirstName();
        lastName = user.getLastName();

        ProfileDTO profileDTO = new ProfileDTO( firstName, lastName );

        return profileDTO;
    }
}
