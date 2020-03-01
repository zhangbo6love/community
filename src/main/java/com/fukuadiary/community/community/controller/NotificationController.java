package com.fukuadiary.community.community.controller;

import com.fukuadiary.community.community.dto.NotificationDTO;
import com.fukuadiary.community.community.enums.NotificationTypeEnum;
import com.fukuadiary.community.community.model.User;
import com.fukuadiary.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);


        return "redirect:/question/" + notificationDTO.getOuterId();


    }
}
