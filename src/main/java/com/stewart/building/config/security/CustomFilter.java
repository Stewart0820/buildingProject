package com.stewart.building.config.security;

import com.stewart.building.mbg.pojo.Menu;
import com.stewart.building.mbg.pojo.Role;
import com.stewart.building.mbg.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.security.access.SecurityConfig;
import java.util.Collection;
import java.util.List;


/**
 *
 * 权限控制
 * 根据请求的url分析所需要的角色
 * @author Stewart
 * @create 2021/12/16
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 白名单的访问不会走这里
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求的url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();

        //获取所有的url
        List<Menu> menus = menuService.getMenuWithRole();
        for (Menu menu : menus) {
            // 判断请求url与菜单里面的url是否匹配
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                //根据url获取roles
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                // 把获取的roles存到security中
                return org.springframework.security.access.SecurityConfig.createList(str);
            }
        }
        // 只要登录成功，默认给的角色 ROLE_LOGIN
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
