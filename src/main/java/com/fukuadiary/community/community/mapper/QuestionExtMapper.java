package com.fukuadiary.community.community.mapper;

import com.fukuadiary.community.community.dto.QuestionQueryDTO;
import com.fukuadiary.community.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}