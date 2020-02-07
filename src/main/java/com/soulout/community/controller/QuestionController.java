package com.soulout.community.controller;

import com.soulout.community.dto.CommentDTO;
import com.soulout.community.dto.QuestionDTO;
import com.soulout.community.enums.CommentTypeEnum;
import com.soulout.community.service.CommentService;
import com.soulout.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name= "id") Long id,
                           Model model){

        QuestionDTO questionDTO = questionService.getById(id);

        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        //累加阅读数
        questionService.incView(id);
        //一级评论列表
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
