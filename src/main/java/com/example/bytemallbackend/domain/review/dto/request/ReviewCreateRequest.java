package com.example.bytemallbackend.domain.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateRequest {

    private Long orderItemId;

    private Integer rating;

    private String content;

}
