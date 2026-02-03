package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Log;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.model.dto.UserDtoResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-02T10:51:20+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;

    @Override
    public Log.Review toReviewEntity(ReviewDtoRequest reviewDtoRequest) {
        if ( reviewDtoRequest == null ) {
            return null;
        }

        Log.Review review = new Log.Review();

        review.setId( reviewDtoRequest.id() );
        review.setRating( reviewDtoRequest.rating() );
        review.setReviewText( reviewDtoRequest.reviewText() );
        review.setReviewDate( reviewDtoRequest.reviewDate() );

        return review;
    }

    @Override
    public ReviewDtoResponse toReviewDtoResponse(Log.Review review) {
        if ( review == null ) {
            return null;
        }

        Long id = null;
        BookDtoResponse book = null;
        UserDtoResponse user = null;
        Double rating = null;
        String reviewText = null;
        LocalDate reviewDate = null;

        id = review.getId();
        book = bookMapper.toBookDtoResponse( review.getBook() );
        user = userMapper.toUserDtoResponse( review.getUser() );
        rating = review.getRating();
        reviewText = review.getReviewText();
        reviewDate = review.getReviewDate();

        ReviewDtoResponse reviewDtoResponse = new ReviewDtoResponse( id, book, user, rating, reviewText, reviewDate );

        return reviewDtoResponse;
    }

    @Override
    public List<ReviewDtoResponse> toReviewDtoResponse(List<Log.Review> review) {
        if ( review == null ) {
            return null;
        }

        List<ReviewDtoResponse> list = new ArrayList<ReviewDtoResponse>( review.size() );
        for ( Log.Review review1 : review ) {
            list.add( toReviewDtoResponse( review1 ) );
        }

        return list;
    }
}
