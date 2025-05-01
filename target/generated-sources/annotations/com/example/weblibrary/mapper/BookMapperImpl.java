package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-30T22:40:55+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public Book toBookEntity(BookDtoRequest bookDtoRequest) {
        if ( bookDtoRequest == null ) {
            return null;
        }

        Book book = new Book();

        book.setTitle( bookDtoRequest.title() );
        book.setPublisher( bookDtoRequest.publisher() );
        book.setIsbn( bookDtoRequest.isbn() );
        book.setPages( bookDtoRequest.pages() );
        book.setGenre( bookDtoRequest.genre() );
        book.setPublishDate( bookDtoRequest.publishDate() );
        book.setLanguage( bookDtoRequest.language() );
        book.setDescription( bookDtoRequest.description() );
        book.setImageUrl( bookDtoRequest.imageUrl() );
        book.setRating( bookDtoRequest.rating() );
        book.setReadUrl( bookDtoRequest.readUrl() );

        return book;
    }

    @Override
    public BookDtoResponse toBookDtoResponse(Book book) {
        if ( book == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String publisher = null;
        String isbn = null;
        Integer pages = null;
        String genre = null;
        LocalDate publishDate = null;
        String language = null;
        String description = null;
        String imageUrl = null;
        String readUrl = null;
        Double rating = null;
        List<AuthorDtoResponse> authors = null;

        id = book.getId();
        title = book.getTitle();
        publisher = book.getPublisher();
        isbn = book.getIsbn();
        pages = book.getPages();
        genre = book.getGenre();
        publishDate = book.getPublishDate();
        language = book.getLanguage();
        description = book.getDescription();
        imageUrl = book.getImageUrl();
        readUrl = book.getReadUrl();
        rating = book.getRating();
        authors = authorMapper.toAuthorDtoResponse( book.getAuthors() );

        List<Long> authorIds = null;

        BookDtoResponse bookDtoResponse = new BookDtoResponse( id, title, publisher, isbn, pages, genre, publishDate, language, description, imageUrl, readUrl, rating, authorIds, authors );

        return bookDtoResponse;
    }

    @Override
    public List<BookDtoResponse> toBookDtoResponse(List<Book> books) {
        if ( books == null ) {
            return null;
        }

        List<BookDtoResponse> list = new ArrayList<BookDtoResponse>( books.size() );
        for ( Book book : books ) {
            list.add( toBookDtoResponse( book ) );
        }

        return list;
    }
}
