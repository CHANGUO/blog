package com.guo.service.impl;

import com.guo.Dao.TypeRepository;
import com.guo.NotFoundException;
import com.guo.po.Type;
import com.guo.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository repository;


    @Transactional
    @Override
    public Type saveType(Type type) {
        return repository.save(type);
    }


    @Transactional
    @Override
    public Type getType(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = repository.findOne(id);
        if (t == null) {
            throw new NotFoundException("标签ID,不存在");
        }

        BeanUtils.copyProperties(type, t);
        return this.saveType(t);
    }

    @Override
    public List<Type> listType() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        repository.delete(id);
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0,size,sort);
        return repository.findTop(pageable);
    }
}
