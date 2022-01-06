package com.stewart.building.mbg.service.impl;

import com.stewart.building.mbg.pojo.User;
import com.stewart.building.mbg.mapper.UserMapper;
import com.stewart.building.mbg.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Stewart
 * @since 2022-01-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
