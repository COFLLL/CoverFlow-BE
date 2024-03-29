package com.coverflow.feedback.dto.response;

import com.coverflow.feedback.dto.FeedbackDTO;

import java.util.List;

public record FeedbackResponse(
        int totalPages,
        long totalElements,
        List<FeedbackDTO> feedbacks
) {
    public static FeedbackResponse of(
            final int totalPages,
            final long totalElements,
            final List<FeedbackDTO> feedbacks
    ) {
        return new FeedbackResponse(totalPages, totalElements, feedbacks);
    }
}
