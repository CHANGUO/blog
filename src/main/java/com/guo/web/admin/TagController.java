package com.guo.web.admin;


import com.guo.po.Tag;
import com.guo.service.TagService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }


    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }


    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Tag t = tagService.getTagByName(tag.getName());

        if (t != null) {
            result.rejectValue("name", "nameError", "标签名称不能重复");
        }

        if (result.hasErrors()) {
            return "admin/tags-input";
        }

        t = tagService.updateTag(id, tag);
        String str = "更新成功";
        if (t == null)
            str = "更新失败";

        attributes.addFlashAttribute("Message", str);
        return "redirect:/admin/tags";
    }


    //删除
    @GetMapping("/tags/{id}/delete")
    public String deleteInput(@PathVariable Long id, RedirectAttributes attributes) {

        tagService.deleteTag(id);
        attributes.addFlashAttribute("Message", "删除成功");
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag t = tagService.getTagByName(tag.getName());
        String str = "添加成功！";

        if (t != null) {
            result.rejectValue("name", "nameError", "标签不能重复添加");
        }


        if (result.hasErrors()) {
            return "admin/tags-input";
        }

        t = tagService.saveTag(tag);


        if (t == null) {
            //新增失败
            str = "操作失败";
        }

        attributes.addFlashAttribute("Message", str);
        return "redirect:/admin/tags";
    }
}
