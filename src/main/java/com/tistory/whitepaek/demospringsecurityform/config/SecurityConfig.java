package com.tistory.whitepaek.demospringsecurityform.config;

import com.tistory.whitepaek.demospringsecurityform.account.AccountService;
import com.tistory.whitepaek.demospringsecurityform.common.LoggingFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;

    public SecurityConfig(AccountService accountService) {
        this.accountService = accountService;
    }

    public SecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class);

        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .mvcMatchers("/user").hasRole("USER")
                .anyRequest().authenticated()
                .expressionHandler(expressionHandler());

        http.formLogin().loginPage("/login").permitAll();

        http.httpBasic();

        http.logout().logoutSuccessUrl("/");

        http.rememberMe().userDetailsService(accountService).key("remember-me-sample"); // 토큰 기반으로 인증

//        http.csrf().disable(); // CSRF 필터 비활성화

//        http.sessionManagement().sessionFixation().changeSessionId().invalidSessionUrl("/login"); // 유효하지 않은 세션에 대한 리디렉션 URL 설정
//        http.sessionManagement().sessionFixation().changeSessionId().maximumSessions(1).maxSessionsPreventsLogin(false); // 동시성 제어 (추가 로그인 설정)
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // STATELESS REST API에 사용하는 전략, 폼 기반의 인증을 지원할 때는 Session을 사용 (세션 생성 전략)

        // TODO ExceptionTranslationFilter(Before) -> FilterSecurityInterceptor(After), (AccessDecisionManager, AffirmativeBased)
        // TODO AuthenticationException -> AuthenticationEntryPoint
        // TODO AccessDeniedException -> AccessDeniedHandler
        // 유저가 권한이 없는 페이지에 접속했을 경우, 서버에 로그를 남기고 특정 페이지로 이동 (Test code - Line 60 ~ 65)
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.getUsername();
            System.out.println(username + " is denied to access - " + request.getRequestURI());
            response.sendRedirect("/access-denied");
        });

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

}