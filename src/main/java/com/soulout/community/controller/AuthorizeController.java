package com.soulout.community.controller;

import com.soulout.community.dto.AccesstTokenDTO;
import com.soulout.community.dto.GithubUser;
import com.soulout.community.model.User;
import com.soulout.community.provider.GithubProvider;
import com.soulout.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @Autowired
    private UserService userService;

    @GetMapping("/github/callback")
    public String gitHubCallback(@RequestParam(name="code") String code,
                                 @RequestParam(name="state") String state,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        AccesstTokenDTO accesstTokenDTO = new AccesstTokenDTO();
        accesstTokenDTO.setClient_id(clientId);
        accesstTokenDTO.setClient_secret(clientSecret);
        accesstTokenDTO.setRedirect_uri(redirectUrl);
        accesstTokenDTO.setCode(code);
        accesstTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(githubUser != null && githubUser.getId() != null){

            //
            //将用户数据存入数据库
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));

            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            //登录成功，写session cookie
            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");

            response.addCookie(cookie);

            return "redirect:/";
        }else{
            log.error("callback get github error,{}",githubUser);
            //登录失败重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //删除cookie和session
        request.getSession().removeAttribute("user");

        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
