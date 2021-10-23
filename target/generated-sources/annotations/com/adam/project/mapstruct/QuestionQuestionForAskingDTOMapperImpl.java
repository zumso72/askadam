package com.adam.project.mapstruct;

import com.adam.project.model.Question;
import com.adam.project.model.dto.QuestionForAskingDTO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-23T16:46:32+0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class QuestionQuestionForAskingDTOMapperImpl implements QuestionQuestionForAskingDTOMapper {

    @Override
    public Question questionForAskingDTOtoQuestion(QuestionForAskingDTO questionForAskingDTO) {
        if ( questionForAskingDTO == null ) {
            return null;
        }

        Question question = new Question();

        question.setQuestion( questionForAskingDTO.getQuestion() );
        question.setAnonymous( questionForAskingDTO.isAnonymous() );
        question.setTopic( questionForAskingDTO.getTopic() );

        return question;
    }
}
