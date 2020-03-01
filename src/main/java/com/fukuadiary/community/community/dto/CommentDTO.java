package com.fukuadiary.community.community.dto;

import com.fukuadiary.community.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentor;
    private Long gmtCreate;
    private Long gmtModdified;
    private Integer likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
