package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.AnswerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndAnswerStatus(
            final long id,
            final AnswerStatus answerStatus
    );

    @Query("""
            SELECT a
            FROM Answer a
            WHERE a.question.id = :questionId
            AND a.answerStatus = 'REGISTRATION'
            """)
    Optional<Page<Answer>> findByQuestionIdAndAnswerStatus(
            final Pageable pageable,
            @Param("questionId") final long questionId
    );

    @Query("""
            SELECT a
            FROM Answer a
            WHERE a.member.id = :memberId
            AND a.answerStatus = 'REGISTRATION'
            ORDER BY a.createdAt DESC
            """)
    Optional<Page<Answer>> findRegisteredAnswers(
            final Pageable pageable,
            @Param("memberId") final UUID memberId
    );

    @Query("""
            SELECT a
            FROM Answer a
            """)
    Optional<Page<Answer>> find(final Pageable pageable);

    @Query("""
            SELECT a
            FROM Answer a
            WHERE a.answerStatus = :answerStatus
            """)
    Optional<Page<Answer>> findByAnswerStatus(
            final Pageable pageable,
            @Param("answerStatus") final AnswerStatus answerStatus
    );

    void deleteByMemberId(UUID id);
}
