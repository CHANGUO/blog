package com.guo.web;


import com.guo.po.Comment;
import com.guo.po.User;
import com.guo.service.BlogService;
import com.guo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController
{


    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){

        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));

        return "blog  :: commentList";
    }



    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        comment.setBlog(blogService.getBlog(comment.getBlog().getId()));
        User user = (User) session.getAttribute("LOGIN_USER");
        if(user != null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
         /*   comment.setNickname(user.getNickname());*/
        }else {
            comment.setAvatar(avatar);
        }


        commentService.saveComment(comment);
        return "redirect:/comments/"+ comment.getBlog().getId();
    }



}
