package com.fukuadiary.community.community.controller;

import com.fukuadiary.community.community.dto.AccessTokenDTO;
import com.fukuadiary.community.community.dto.GithutUser;
import com.fukuadiary.community.community.model.User;
import com.fukuadiary.community.community.provider.GithubProvider;
import com.fukuadiary.community.community.service.UserService;
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

    @Autowired
    private UserService userService;


    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessTokenDTO(accessTokenDTO);
        GithutUser githutUser = githubProvider.getUser(accessToken);
        if (githutUser != null && githutUser.getId() != null) {
            User user = new User();
            user.setAccountId(String.valueOf(githutUser.getName()));
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githutUser.getName());
            user.setAvatarUrl(githutUser.getAvatar_url());
            userService.creatorUpdate(user);

            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            log.error("callback get github error,{}", githutUser);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
