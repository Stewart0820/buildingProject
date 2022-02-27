package com.stewart.building.mbg.controller.user.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stewart.building.common.R;
import com.stewart.building.common.ResultStatus;
import com.stewart.building.mbg.pojo.*;
import com.stewart.building.mbg.service.IMenuService;
import com.stewart.building.mbg.service.IRoleMenuService;
import com.stewart.building.mbg.service.IUserService;
import com.stewart.building.param.user.UserLoginParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.scene.control.MenuItem;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public R login(@RequestBody UserLoginParam userLoginParam, HttpServletRequest request) {
        return userService.login(userLoginParam.getAccount(), userLoginParam.getPassword(), request);
    }

    @ApiOperation(value = "获取当前登陆用户的信息")
    @GetMapping("/getInfo/{id}")
    public R getUserInfo(@PathVariable Integer id) {
        User user = userService.getInfo(id);
        user.setPassword(null);

        //设置角色
        user.setRoles(userService.getRoles(user.getId()));

        //根据角色查询所有的左侧菜单栏
        Integer roleId = user.getRoles().get(0).getId();

        List<RoleMenu> menuList = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        List<Menus> collect = menuList.stream().map(item -> {
            Menu menu1 = menuService.getOne(new QueryWrapper<Menu>().eq("id", item.getMenuId()));
            Menus menus1 = getAllMenus(menu1);
            Integer menuFatherId = menu1.getId();
            List<Menu> menuSonId = menuService.list(new QueryWrapper<Menu>().eq("pid", menuFatherId));
            //获取子类的menu
            List<Menus> menusList = menuSonId.stream().map(i -> {
                Menu menu2 = menuService.getOne(new QueryWrapper<Menu>().eq("id", i.getId()));
                return getAllMenus(menu2);
            }).collect(Collectors.toList());

            menus1.setChildren(menusList);
            return menus1;
        }).collect(Collectors.toList());

        user.setMenus(collect);
        return R.ok(ResultStatus.GAIN_SUCCESS, user);

    }

    /**
     * 封装的menu对象的转换
     * @param menu
     * @return
     */
    private static Menus getAllMenus(Menu menu) {
        Menus menus = new Menus();
        Meta meta = new Meta();
        BeanUtils.copyProperties(menu, menus);
        BeanUtils.copyProperties(menu, meta);
        menus.setMeta(meta);
        return menus;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout() {
        //这里返回200成功给前端，前端直接把请求头里面的token删除
        return R.ok(ResultStatus.LOGOUT_SUCCESS);
    }

}
