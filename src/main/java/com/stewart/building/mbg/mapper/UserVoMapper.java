package com.stewart.building.mbg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stewart.building.mbg.pojo.UserVo;

import java.util.List;

/**
 * @author 陈鸿杰
 * @create 2021/12/23
 * @Description
 */
public interface UserVoMapper extends BaseMapper<UserVo> {

    /**
     * 批量添加学生
     * @param datas
     * @return
     */
    int batchInsert(List<Object> datas);
}


