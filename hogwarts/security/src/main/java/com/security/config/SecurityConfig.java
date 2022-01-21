package com.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 接口请求授权
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**", "/login/**","/logout/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .csrf().disable();
    }

    /**
     * 描述: 静态资源放行，这里的放行，是不走 Spring Security 过滤器链
     **/
    @Override
    public void configure(WebSecurity web) {
        // 可以直接访问的静态数据
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/404.html")
                .antMatchers("/500.html")
                .antMatchers("/html/**")
                .antMatchers("/js/**");
    }

    /**
     * 描述: 密码加密算法 BCrypt 推荐使用
     **/
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 描述: 注入AuthenticationManager管理器
     **/
    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {

        *//*InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String passwordAdminAfterEncoder = passwordEncoder.encode("admin");
        String passwordGuestAfterEncoder = passwordEncoder.encode("guest");
        manager.createUser(User.withUsername("admin").password(passwordAdminAfterEncoder).roles("").build());
        manager.createUser(User.withUsername("guest").password(passwordGuestAfterEncoder).roles("").build());
        return manager;*//*

     *//*return username -> {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("username", username);
            com.security.entity.User user = userMapper.selectOne(queryWrapper);
            log.info("用户信息{}", user);
            if (user == null) {
                throw new UsernameNotFoundException("用户名未找到");
            }
            String password = user.getPassword();
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String passwordAfterEncoder = passwordEncoder.encode(password);

            log.info(username + "---->>>" + passwordAfterEncoder);

            return User.withUsername(username).password(passwordAfterEncoder).roles("").build();
        };*//*


        return username -> {
            //查询用户
            QueryWrapper userWrapper = new QueryWrapper();
            userWrapper.eq("username", username);
            com.security.entity.User user = userMapper.selectOne(userWrapper);
            log.info("用户信息{}", user);
            if (user == null) {
                throw new UsernameNotFoundException("用户名未找到");
            }
            //密码加密
            String password = user.getPassword();
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String passwordAfterEncoder = passwordEncoder.encode(password);
            log.info(username + "/" + passwordAfterEncoder);

            //查询用户角色角色
            QueryWrapper userRoleWrapper = new QueryWrapper();
            userRoleWrapper.eq("uid", user.getId());
            UserRole userRole = userRoleMapper.selectOne(userRoleWrapper);
            log.info("用户角色{}", userRole);

            //查询角色信息
            QueryWrapper roleWrapper = new QueryWrapper();
            roleWrapper.eq("id", userRole.getRid());
            Role role = roleMapper.selectOne(roleWrapper);
            log.info("角色信息{}", role);

            *//*String[] permissionArr = new String[roles.size() + permissions.size()];
            int permissionArrIndex = 0;
            for (String role : roles) {
                permissionArr[permissionArrIndex] = "ROLE_" + role;
                permissionArrIndex++;
            }
            for (String permission : permissions) {
                permissionArr[permissionArrIndex] = permission;
                permissionArrIndex++;
            }
            return User.withUsername(username).password(passwordAfterEncoder).authorities(permissionArr).build();*//*
            return User.withUsername(username).password(passwordAfterEncoder).roles("").build();
        };
    }*/

}
