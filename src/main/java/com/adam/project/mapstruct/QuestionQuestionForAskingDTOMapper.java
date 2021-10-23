package com.adam.project.mapstruct;

import com.adam.project.model.Question;
import com.adam.project.model.dto.QuestionForAskingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface QuestionQuestionForAskingDTOMapper {
    Question questionForAskingDTOtoQuestion(QuestionForAskingDTO questionForAskingDTO);
}
