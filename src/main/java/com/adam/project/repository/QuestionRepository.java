package com.adam.project.repository;

import com.adam.project.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "select  * from questions where author_id = ?1 order by answer_time desc"
            , nativeQuery = true)
    List<Question> getQuestionsByAuthor(Long id);
    @Query(value = "select  * from questions where author_id = ?1 AND is_anonymous = 'false' order by answer_time"
            , nativeQuery = true)
    List<Question> getQuestionsByAuthorWhereIsAnonymousFalse(Long id);
    List<Question> getQuestionByAnswerIsNullOrderByTimeDesc();
    @Query(value = "select  * from questions where answer is not null  and is_anonymous = 'false' limit 7"
            , nativeQuery = true)
    List<Question> getQuestionsByAnswerIsNotNull();
}
