package com.example.weblibrary.mapper;

import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-22T18:56:57+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUserEntity(UserDtoRequest userDtoRequest) {
        if ( userDtoRequest == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDtoRequest.id() );
        user.setName( userDtoRequest.name() );
        user.setSurname( userDtoRequest.surname() );
        user.setPatronymic( userDtoRequest.patronymic() );
        user.setAge( userDtoRequest.age() );
        user.setSubscription( userDtoRequest.subscription() );
        user.setSex( userDtoRequest.sex() );
        user.setCountry( userDtoRequest.country() );
        user.setEmail( userDtoRequest.email() );
        user.setPasswordHash( userDtoRequest.passwordHash() );

        return user;
    }

    @Override
    public UserDtoResponse toUserDtoResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String surname = null;
        String patronymic = null;
        Integer age = null;
        String subscription = null;
        String sex = null;
        String country = null;
        String email = null;
        String passwordHash = null;
        LocalDate registrationDate = null;
        LocalDate lastLogin = null;

        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        patronymic = user.getPatronymic();
        age = user.getAge();
        subscription = user.getSubscription();
        sex = user.getSex();
        country = user.getCountry();
        email = user.getEmail();
        passwordHash = user.getPasswordHash();
        registrationDate = user.getRegistrationDate();
        lastLogin = user.getLastLogin();

        String favoriteBooks = null;

        UserDtoResponse userDtoResponse = new UserDtoResponse( id, name, surname, patronymic, age, subscription, sex, country, favoriteBooks, email, passwordHash, registrationDate, lastLogin );

        return userDtoResponse;
    }

    @Override
    public List<UserDtoResponse> toUserDtoResponse(List<User> user) {
        if ( user == null ) {
            return null;
        }

        List<UserDtoResponse> list = new ArrayList<UserDtoResponse>( user.size() );
        for ( User user1 : user ) {
            list.add( toUserDtoResponse( user1 ) );
        }

        return list;
    }
}
