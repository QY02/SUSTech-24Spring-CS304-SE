package org.cs304.backend.service.impl;

import org.cs304.backend.entity.User;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
