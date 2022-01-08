package com.stewart.building.mbg.controller.user.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stewart.building.common.R;
import com.stewart.building.common.ResultStatus;
import com.stewart.building.common.renum.ReturnEnum;
import com.stewart.building.mbg.pojo.UserVo;
import com.stewart.building.mbg.service.IUserService;
import com.stewart.building.param.user.teacher.AddTeacherParam;
import com.stewart.building.param.user.teacher.GetAllTeacherByPageParam;
import com.stewart.building.param.user.teacher.UpdateTeacherParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author 陈鸿杰
 * @create 2022/1/8
 * @Description
 */
@Api(tags = "老师模块")
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private IUserService userService;



    @ApiOperation(value = "分页获取所有的老师")
    @PostMapping("/getAll")
    public R getAll(@Valid @RequestBody GetAllTeacherByPageParam teacherByPageParam){
        Page<UserVo> result = userService.getAll(teacherByPageParam);
        return R.ok(ResultStatus.SELECT_SUCCESS,result);
    }

    @ApiOperation(value = "添加老师")
    @PostMapping("/add")
    public R addTeacher(@Valid @RequestBody AddTeacherParam addTeacherParam){
        return userService.addTeacher(addTeacherParam);
    }

    @ApiOperation(value = "修改老师")
    @PostMapping("/update")
    public R updateTeacher(@Valid @RequestBody UpdateTeacherParam param){
        return userService.updateTeacher(param);
    }
}
