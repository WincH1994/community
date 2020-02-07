package com.soulout.community.mapper;

import com.soulout.community.model.Comment;
import com.soulout.community.model.CommentExample;
import com.soulout.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}