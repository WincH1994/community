package com.soulout.community.dto;

import com.soulout.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Integer id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModify;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
