package com.stewart.building.mbg.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stewart.building.common.R;
import com.stewart.building.common.ResultStatus;
import com.stewart.building.common.renum.ReturnEnum;
import com.stewart.building.common.renum.RoleDetailEnum;
import com.stewart.building.config.component.JwtTokenUtil;
import com.stewart.building.mbg.mapper.*;
import com.stewart.building.mbg.pojo.*;
import com.stewart.building.mbg.service.IUserService;
import com.stewart.building.param.user.teacher.AddTeacherParam;
import com.stewart.building.param.user.teacher.GetAllTeacherByPageParam;
import com.stewart.building.param.user.teacher.UpdateTeacherParam;
import com.stewart.building.util.BeanConvert;
import com.stewart.building.util.IpUtil;
import com.stewart.building.util.ObjectUtils;
import com.stewart.building.util.UserUtil;
import com.stewart.building.vo.user.GetAllTeacherVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ChenHongjie
 * @since 2021-12-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Value("${user.password}")
    private String userPassword;

    @Autowired
    private UserLogMapper userLogMapper;

    @Autowired
    private UserVoMapper userVoMapper;

    /**
     * 登录之后返回token
     *
     * @param account
     * @param password
     * @param request
     * @return
     */
    @Override
    @Transactional
    public R login(String account, String password, HttpServletRequest request) {
        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(account);

        //判断账号密码是否正确
        if (userDetails == null || passwordEncoder.matches(password, userDetails.getPassword())) {
            return R.error(ResultStatus.USERNAME_ERROR);
        }

        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("id", UserUtil.getUserId().toString());

        UserLog userLog = new UserLog();
        userLog.setUserId(UserUtil.getUserId())
                .setCreateTime(ObjectUtils.CurrentTime())
                .setToken(token)
                .setIp(IpUtil.getIpAddr(request));
        return R.ok(ResultStatus.LOGIN_SUCCESS, tokenMap);
    }


    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer userId) {
        return roleMapper.getRoles(userId);

    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public User getInfo(Integer userId) {
        return userMapper.selectById(userId);
    }


    /**
     * 分页/模糊 查询所有的老师
     *
     * @param param
     * @return
     */
    @Override
    public Page<UserVo> getAll(GetAllTeacherByPageParam param) {
        Page<UserVo> page = new Page<>(param.getCurrent(), param.getSize());
        Page<UserVo> userVos = userVoMapper.selectPage(page,
                new LambdaQueryWrapper<UserVo>()
                        .like(UserVo::getName, param.getName())
                        .like(UserVo::getAccount, param.getAccount())
                        .select(UserVo::getId,
                                UserVo::getAccount,
                                UserVo::getGender,
                                UserVo::getName,
                                UserVo::getGender,
                                UserVo::getType,
                                UserVo::getMobile));
        log.error(userVos.getRecords()+"");
        List<UserVo> records = userVos.getRecords();
        //转换bean
        List<GetAllTeacherVo> getAllTeacherByPageParams = BeanConvert.convertList2List(records, GetAllTeacherVo.class);
        Page<GetAllTeacherVo> teacherPage = new Page<>();
        BeanUtils.copyProperties(userVos,teacherPage);
        teacherPage.setRecords(getAllTeacherByPageParams);
        return userVos;
    }


    /**
     * 根据用户的账号获取用户信息
     *
     * @param account
     * @return
     */
    @Override
    public User getUserByUsername(String account) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("account", account));
    }

    /**
     * 修改老师
     * @param param
     * @return
     */
    @Override
    @Transactional
    public R updateTeacher(UpdateTeacherParam param) {
        UserVo userVo = userVoMapper.selectById(param.getId());
        if(StringUtils.isEmpty(userVo)){
            return R.error(ResultStatus.ID_NOT_EXIST);
        }
        UserVo user = new UserVo();
        if(param.getType()!= userVo.getType()){
            //修改角色信息
            List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                    .eq(UserRole::getUserId, param.getId())
                    .select(UserRole::getId));
            for(UserRole role:userRoles){
                userRoleMapper.deleteById(role.getId());
            }
            addUserRole(param.getType(),userRoleMapper,param.getId());
        }
        BeanUtils.copyProperties(param,user);
        user.setUpdateTime(ObjectUtils.CurrentTime());
        int res = userVoMapper.updateById(user);
        if (res==1){
            return R.ok(ResultStatus.UPDATE_SUCCESS);
        }else{
            return R.error(ResultStatus.UPDATE_ERROR);
        }
    }

    /**
     * 在userRole表中添加数据
     * @param userType
     * @param userRoleMapper
     * @param userId
     */
    private static void addUserRole(Integer userType,UserRoleMapper userRoleMapper,Integer userId){
        insertTeacher(userId, userType, userRoleMapper);
    }
    /**
     * 创建一个userRole对象
     *
     * @param userId 用户id
     * @param roleId 用户具有什么角色
     * @return
     */
    private static UserRole getUserRole(Integer userId, Integer roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        logger.info(userRole + "");
        return userRole;
    }
    /**
     * 添加老师
     *
     * @param addTeacherParam
     * @return
     */
    @Override
    @Transactional
    public R addTeacher(AddTeacherParam addTeacherParam) {
        //判断电话是否存在
        User mobileExist = userMapper.selectOne(new QueryWrapper<User>().eq("mobile", addTeacherParam.getMobile()));
        logger.warn(mobileExist + "");
        if (!BeanUtil.isEmpty(mobileExist)) {
            return R.error(ResultStatus.MOBILE_EXIST);
        }
        //判断邮箱是否存在
        User emailExist = userMapper.selectOne(new QueryWrapper<User>().eq("email", addTeacherParam.getEmail()));
        if (!BeanUtil.isEmpty(emailExist)) {
            return R.error(ResultStatus.EMAIL_EXIST);
        }
        User user = new User();
        BeanUtils.copyProperties(addTeacherParam, user);
        user.setCreateTime(ObjectUtils.CurrentTime())
                .setAccount(addTeacherParam.getMobile())
                .setPassword(passwordEncoder.encode(userPassword));
        userMapper.insert(user);
        //查询存进的user
        User userResult = userMapper.selectOne(new QueryWrapper<User>().eq("mobile", user.getAccount()));
        Integer userId = userResult.getId();
        Integer userType = user.getType();
        insertTeacher(userId, userType, userRoleMapper);
        return R.ok(ResultStatus.ADD_SUCCESS);
    }

    private static void insertTeacher(Integer userId, Integer userType, UserRoleMapper userRoleMapper) {
        if (userType == RoleDetailEnum.TEACHER.ordinal()) {
            userRoleMapper.insert(getUserRole(userId, RoleDetailEnum.TEACHER.ordinal()));
        } else if (userType == RoleDetailEnum.TEACHER_LAB.ordinal()) {
            userRoleMapper.insert(getUserRole(userId, RoleDetailEnum.TEACHER_LAB.ordinal()));
        } else {
            userRoleMapper.insert(getUserRole(userId, RoleDetailEnum.TEACHER.ordinal()));
            userRoleMapper.insert(getUserRole(userId, RoleDetailEnum.TEACHER_LAB.ordinal()));
        }
    }
}
