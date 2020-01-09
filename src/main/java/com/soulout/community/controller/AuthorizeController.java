package com.soulout.community.controller;

import com.soulout.community.dto.AccesstTokenDTO;
import com.soulout.community.dto.GithubUser;
import com.soulout.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/github/callback")
    public String gitHubCallback(@RequestParam(name="code") String code,@RequestParam(name="state") String state){
        AccesstTokenDTO accesstTokenDTO = new AccesstTokenDTO();
        accesstTokenDTO.setClient_id("bc4c1e4efac90a3b8692");
        accesstTokenDTO.setClient_secret("6c52a2c9942690b4e8d9ed962bd42309da904acf");
        accesstTokenDTO.setRedirect_uri("http://127.0.0.1:8888/github/callback");
        accesstTokenDTO.setCode(code);
        accesstTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);

        //数据保存github用户数据







        return "index";
    }
}
