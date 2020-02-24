package com.soulout.community.controller;

import com.soulout.community.cache.HotTagCache;
import com.soulout.community.dto.PaginationDTO;
import com.soulout.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VideoController {

    @GetMapping("/videos")
    public String index(){
        return "videos";
    }

    @GetMapping("/video/{id}")
    public String video(@PathVariable(name = "id") String id){
        return "video";
    }
}
