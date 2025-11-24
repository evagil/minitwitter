package unrn.mapper;

import unrn.dto.UserDto;
import unrn.model.User;

public class UserMapper {

    public static UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setUserName(u.getUserName());
        return dto;
    }
}

