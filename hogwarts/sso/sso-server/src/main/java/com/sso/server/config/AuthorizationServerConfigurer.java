package com.sso.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *
     * 认证服务器最终是以api接⼝的⽅式对外提供服务（校验合法性并⽣成令牌、校验令牌等）
     * 那么，以api接⼝⽅式对外的话，就涉及到接⼝的访问权限，我们需要在这⾥进⾏必要的配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        security.allowFormAuthenticationForClients()    // 允许客户端表单认证
                .tokenKeyAccess("permitAll()")  // 开启端口/oauth/token_key的访问权限（允许）
                .checkTokenAccess("permitAll()");   // 开启端口/oauth/check_token的访问权限（允许）
        //security.tokenKeyAccess("isAuthenticated()");
    }

    /**
     * 客户端详情配置
     * ⽐如client_id，secret
     * 当前这个服务就如同QQ平台，本网站作为客户端需要qq平台进⾏登录授权认证等，提前需到QQ平台注册，QQ平台会给我们
     * 颁发client_id等必要参数，表明客户端是谁
     * @param clients   client  配置
     * @throws Exception    异常信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //将获取code和token合并到一步中 http://localhost:8111/sso/oauth/authorize?response_type=code&client_id=order-client&scope=all
        //首先获取授权code，然后通过code获取token http://localhost:8111/sso/oauth/authorize?response_type=code&client_id=user-client&scope=all&redirect_uri=http://www.baidu.com
        //使用 code 获取 token  http://localhost:8111/sso/oauth/token?grant_type=authorization_code&code=DctdGQ&username=admin&password=123456&client_id=user-client&client_secret=123456&redirect_uri=http://www.baidu.com
        //密码模式 ：http://localhost:8111/sso/oauth/token?grant_type=password&username=admin&password=123456&client_id=order-client&client_secret=123456
        //简化模式 ：http://localhost:8111/sso/oauth/authorize?response_type=token&client_id=user-client&client_secret=123456&scope=all&redirect_uri=http://www.baidu.com
        //客户端模式 ：http://localhost:8111/sso/oauth/token?client_id=user-client&client_secret=123456&grant_type=client_credentials

        // 定义了两个客户端应用的通行证
        clients.inMemory()
                .withClient("sheep")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password", "implicit", "client_credentials")
                .scopes("all")
                .autoApprove(true)
                .redirectUris("http://localhost:8772/login")
                .and()
                .withClient("order-client")
                .resourceIds("order-server", "user-server", "oauth-server")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password", "implicit", "client_credentials")
                .accessTokenValiditySeconds(3600)
                .scopes("all")
                .autoApprove(true)  //自动确认授权
                //自动认证并跳转获取token
                .redirectUris("http://127.0.0.1:8111/sso/oauth/token?grant_type=authorization_code&client_id=order-client&client_secret=123456")
                .and()
                .withClient("user-client")
                .resourceIds("order-server", "user-server", "oauth-server")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password", "implicit", "client_credentials")
                .accessTokenValiditySeconds(3600)
                .scopes("all")
                .autoApprove(true)
                .redirectUris("http://www.baidu.com")
                .and().build();
        //clients.withClientDetails();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // jwt token 方式
        endpoints.authenticationManager(authenticationManager)
                .tokenServices(authorizationServerTokenServices())
                //.userDetailsService(myUserDetailsService)
                .tokenStore(tokenStore())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("test123");
        converter.setVerifier(new MacSigner("test123"));
        return converter;
    }

    /**
     * 该方法用户获取一个token服务对象（该对象描述了token有效期等信息）
     */
    private AuthorizationServerTokenServices authorizationServerTokenServices() {
        // 使用默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true); // 是否开启令牌刷新
        defaultTokenServices.setTokenStore(tokenStore());

        // 针对jwt令牌的添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());

        // 设置令牌有效时间（一般设置为2个小时）
        defaultTokenServices.setAccessTokenValiditySeconds(2 * 60 * 60); // access_token就是我们请求资源需要携带的令牌
        // 设置刷新令牌的有效时间
        defaultTokenServices.setRefreshTokenValiditySeconds(259200); // 3天

        return defaultTokenServices;
    }
}