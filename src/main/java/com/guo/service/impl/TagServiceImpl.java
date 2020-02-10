package com.guo.service.impl;

import com.guo.Dao.TagRepository;
import com.guo.NotFoundException;
import com.guo.po.Tag;
import com.guo.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;


    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return repository.save(tag);
    }


    @Transactional
    @Override
    public Tag getTag(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = repository.findOne(id);
        if (t == null) {
            throw new NotFoundException("标签ID,不存在");
        }

        BeanUtils.copyProperties(tag, t);
        return this.saveTag(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        repository.delete(id);
    }

    @Override
    public List<Tag> listTag() {
        return repository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        if (ids != null && ids.length() > 0) {
            String[] tag = ids.split(",");
            List<Long> list = new ArrayList<>();
            for (String s : tag){
                list.add(new Long(s));
            }

            return repository.findAll(list);
        }


        return null;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0,size,sort);
        return repository.findTop(pageable);
    }


}
