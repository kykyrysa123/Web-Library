package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-02T10:51:20+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public Author toAuthorEntity(AuthorDtoRequest authorDtoRequest) {
        if ( authorDtoRequest == null ) {
            return null;
        }

        Author author = new Author();

        author.setName( authorDtoRequest.name() );
        author.setSurname( authorDtoRequest.surname() );
        author.setPatronymic( authorDtoRequest.patronymic() );
        author.setBirthDate( authorDtoRequest.birthDate() );
        author.setDeathDate( authorDtoRequest.deathDate() );
        author.setBiography( authorDtoRequest.biography() );
        author.setGenreSpecialization( authorDtoRequest.genreSpecialization() );
        author.setRating( authorDtoRequest.rating() );

        return author;
    }

    @Override
    public AuthorDtoResponse toAuthorDtoResponse(Author author) {
        if ( author == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String surname = null;
        String patronymic = null;
        LocalDate birthDate = null;
        LocalDate deathDate = null;
        String biography = null;
        String genreSpecialization = null;
        Double rating = null;

        id = author.getId();
        name = author.getName();
        surname = author.getSurname();
        patronymic = author.getPatronymic();
        birthDate = author.getBirthDate();
        deathDate = author.getDeathDate();
        biography = author.getBiography();
        genreSpecialization = author.getGenreSpecialization();
        rating = author.getRating();

        AuthorDtoResponse authorDtoResponse = new AuthorDtoResponse( id, name, surname, patronymic, birthDate, deathDate, biography, genreSpecialization, rating );

        return authorDtoResponse;
    }

    @Override
    public List<AuthorDtoResponse> toAuthorDtoResponse(List<Author> authors) {
        if ( authors == null ) {
            return null;
        }

        List<AuthorDtoResponse> list = new ArrayList<AuthorDtoResponse>( authors.size() );
        for ( Author author : authors ) {
            list.add( toAuthorDtoResponse( author ) );
        }

        return list;
    }
}
