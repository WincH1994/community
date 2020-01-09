package com.soulout.community.provider;

import com.alibaba.fastjson.JSON;
import com.soulout.community.dto.AccesstTokenDTO;
import com.soulout.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    public String  getAccessToken(AccesstTokenDTO accesstTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON.toJSONString(accesstTokenDTO), mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            System.out.println(str);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();

            GithubUser githubUser = JSON.parseObject(str, GithubUser.class);


            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}