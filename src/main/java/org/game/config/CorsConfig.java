package org.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 跨域配置
 *
 * @author Zhouyf
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);   //设置是否支持cookie跨域
        config.setAllowedOrigins(Arrays.asList("*"));     //放哪些原始域
        config.setAllowedHeaders(Arrays.asList("*"));       //允许的头
        config.setAllowedMethods(Arrays.asList("*"));       //允许的方法
        config.setMaxAge(3600L);     //缓存时间
//        Access-Control-Max-Age
        source.registerCorsConfiguration("/**", config); //对哪些域名进行配置

        return new CorsFilter(source);
    }

}
