package org.game.filter;

import lombok.extern.slf4j.Slf4j;
import org.game.pojo.User;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 优先级最低的过滤器，用于过滤未登录的请求。
 */
@Configuration
@Component
@Slf4j
public class JudgeZuulRequest {

    @Bean
    public FilterRegistrationBean judgeZuulRequestFilterBean() {
        FilterRegistrationBean judgeZuulRequestFilterBean = new FilterRegistrationBean();
        judgeZuulRequestFilterBean.setFilter(new JudgeZuulRequestFilter());
        judgeZuulRequestFilterBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        List<String> urlPatterns = new ArrayList();
        urlPatterns.add("/*");
        judgeZuulRequestFilterBean.setUrlPatterns(urlPatterns);
        return judgeZuulRequestFilterBean;
    }

    public class JudgeZuulRequestFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) {

        }

        //        @SneakyThrows
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) servletRequest;

            String xAuthToken = req.getHeader("x-auth-token");
            String path = req.getRequestURI();


//            log.error("****************************************************************************");
            log.error("path : " + path);
//            log.error("xAuthToken : " + xAuthToken);
//            log.error("access-control-request-headers : " + req.getHeader("access-control-request-headers"));
//            log.error("****************************************************************************");
            //过滤调预检请求
            if (!StringUtils.isEmpty(req.getHeader("access-control-request-headers"))) {
                filterChain.doFilter(servletRequest, servletResponse);
                log.error("access-control-request-headers is NULL!");
            } else {
                if (path.contains("/user/login")
                        || path.contains("/user/register")
                        || path.contains("/swagger-ui.html")
                        || path.contains("/favicon.ico")
                        || path.contains("/webjars/")
                        || path.contains("/v2/")
                        || path.contains("/swagger-resources")
                        || path.contains("/game/getGameInfoQ")
                        || path.contains("/ranking/findRanking")
                        || path.contains("/game/findGameState")
                )
                    filterChain.doFilter(servletRequest, servletResponse);
                else {
                    if (StringUtils.isEmpty(xAuthToken)) {
                        log.error("请登录后访问！");
                        req.getRequestDispatcher("/user/isNotLogin").forward(req, servletResponse);
                        return;
                    } else {
                        User user = JWTToken.getUserFromJwt(xAuthToken);
                        if (user == null) {
                            req.getRequestDispatcher("/user/userIsNull").forward(req, servletResponse);
                            return;
                        }
                        try {
                            if(!JWTToken.userMap.get(user.getId()).equals(xAuthToken)){
                                log.error("用户已在其它设备登录！");
                                req.getRequestDispatcher("/user/otherLogin").forward(req, servletResponse);
                                return;
                            }
                            if (!JWTToken.isJwtValid(xAuthToken, user)) {
                                log.error("用户登录超时！");
                                req.getRequestDispatcher("/user/loginTimeOut").forward(req, servletResponse);
                            } else {
                                filterChain.doFilter(servletRequest, servletResponse);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error(e.getMessage());
                            req.getRequestDispatcher("/user/userError").forward(req, servletResponse);
                        }
                    }
                }
            }
        }

        @Override
        public void destroy() {

        }

    }
}
