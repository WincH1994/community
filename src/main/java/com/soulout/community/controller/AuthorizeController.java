package com.soulout.community.controller;

import com.soulout.community.dto.AccesstTokenDTO;
import com.soulout.community.dto.GithubUser;
import com.soulout.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @GetMapping("/github/callback")
    public String gitHubCallback(@RequestParam(name="code") String code,@RequestParam(name="state") String state){
        AccesstTokenDTO accesstTokenDTO = new AccesstTokenDTO();
        accesstTokenDTO.setClient_id(clientId);
        accesstTokenDTO.setClient_secret(clientSecret);
        accesstTokenDTO.setRedirect_uri(redirectUrl);
        accesstTokenDTO.setCode(code);
        accesstTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);

        System.out.println(user.getName());
        //数据保存github用户数据







        return "index";
    }
}
