package com.stewart.building.mbg.mapper;

import com.stewart.building.mbg.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Stewart
 * @since 2022-01-06
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenuWithRole();

    /**
     * 根据用户查询菜单列表
     * @param userId
     * @return
     */
    List<Menu> getMenusByUserId(Integer userId);

    /**
     * 查询所有的menu
     * @return
     */
    List<Menu> getAllMenu();
}
