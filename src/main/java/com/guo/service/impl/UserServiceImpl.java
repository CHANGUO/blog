package com.guo.service.impl;

import com.guo.Dao.UserRepository;
import com.guo.po.User;
import com.guo.service.UserService;
import com.guo.uitil.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;
    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findUserByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
