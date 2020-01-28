package com.soulout.community.controller;

import com.soulout.community.dto.AccesstTokenDTO;
import com.soulout.community.dto.GithubUser;
import com.soulout.community.mapper.UserMapper;
import com.soulout.community.model.User;
import com.soulout.community.provider.GithubProvider;
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
    private UserMapper userMapper;

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

        if(githubUser != null){
            //将用户数据存入数据库
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功，写session cookie
            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");

            response.addCookie(cookie);

            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else{
            //登录失败重新登录
            return "redirect:/";
        }
        //数据保存github用户数据





    }
}
