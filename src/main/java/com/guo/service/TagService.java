package com.guo.service;

import com.guo.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);
    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);
}
