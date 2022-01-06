package com.stewart.building.mbg.controller.user.user;

import com.stewart.building.common.R;
import com.stewart.building.common.ResultStatus;
import com.stewart.building.mbg.pojo.User;
import com.stewart.building.mbg.service.IUserService;
import com.stewart.building.param.user.UserLoginParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stewart
 * @create 2021/12/15
 */
@Api(tags = "user模块/login模块")
@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public R login(@RequestBody UserLoginParam userLoginParam, HttpServletRequest request){
        return userService.login(userLoginParam.getAccount(),userLoginParam.getPassword(),request);
    }

    @ApiOperation(value = "获取当前登陆用户的信息")
    @GetMapping("/getInfo/{id}")
    public R getUserInfo(@PathVariable Integer id){
        User user = userService.getInfo(id);
        user.setPassword(null);

        //设置角色
        user.setRoles(userService.getRoles(user.getId()));
        return R.ok(ResultStatus.GAIN_SUCCESS,user);

    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout(){
        //这里返回200成功给前端，前端直接把请求头里面的token删除
        return R.ok(ResultStatus.LOGOUT_SUCCESS);
    }

}
