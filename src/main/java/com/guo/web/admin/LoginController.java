package com.guo.web.admin;

import com.guo.po.User;
import com.guo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {


    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage(){

        return "admin/login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession httpSession,
                        RedirectAttributes redirectAttributes){
        User user = userService.checkUser(username, password);
        if(user != null){
            user.setPassword(null);
            httpSession.setAttribute("LOGIN_USER",user);
            return "admin/index";
        }

        redirectAttributes.addFlashAttribute("Message","账号或者密码错误！");
        return "redirect:/admin";


    }



        /*注销用户*/
        @GetMapping("/logout")
        public String logout(HttpSession httpSession){

            httpSession.removeAttribute("LOGIN_USER");
            return "redirect:/admin";
        }

}
