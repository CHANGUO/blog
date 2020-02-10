package com.guo.web.admin;

import com.guo.po.Blog;
import com.guo.po.User;
import com.guo.service.BlogService;
import com.guo.service.TagService;
import com.guo.service.TypeService;
import com.guo.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class BlogController {


    private final static String INPUT = "admin/blogs-input";
    private final static String LIST = "admin/blogs";
    private final static String REDIRECT_LIST = "redirect:/admin/blogs";


    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;


    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 3, sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        Page<Blog> p = blogService.listBlog(pageable, blog);
        model.addAttribute("page", blogService.listBlog(pageable, blog));

        return LIST;
    }


    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3, sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {

        model.addAttribute("page", blogService.listBlog(pageable, blog));

        return "admin/blogs :: blogList";
    }


    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTage(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTage(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    private void setTypeAndTage(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @PostMapping("/blogs")
    public String post(Long id,Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("LOGIN_USER"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));


        Blog b = blog.getId() == null ? blogService.saveBlog(blog) : blogService.updateBlog(id,blog);

        if (b == null) {
            attributes.addFlashAttribute("Message", "操作失败");
        } else {
            attributes.addFlashAttribute("Message", "操作成功");
        }
        return REDIRECT_LIST;
    }


    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {

        Blog b = blogService.getBlog(id);
        String message = "删除博客失败";

        if(b!=null){

            blogService.deleteBlog(id);
            message = "删除博客<<"+b.getTitle()+">>成功!";
        }

        attributes.addFlashAttribute("Message",message);
        return REDIRECT_LIST;
    }
}