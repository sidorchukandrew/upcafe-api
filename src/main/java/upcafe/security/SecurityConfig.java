package upcafe.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import upcafe.security.exception.RestAuthenticationEntryPoint;
import upcafe.security.service.OAuth2UserService;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2UserService userService;

    @Autowired
    private HttpCookieOAuth2RequestRepository cookieOAuth2RequestRepository;

    @Autowired
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler failureHandler;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/gs-guide-websocket/**",
                        "/favicon.ico",
                        "/api/v2/**")
                .permitAll()
                .antMatchers("/oauth/**")
                .permitAll()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(cookieOAuth2RequestRepository)
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(authorizationCodeTokenResponseClient())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(userService)
                .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = 
      	      new DefaultAuthorizationCodeTokenResponseClient(); 
      	    accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter()); 
      	    return accessTokenResponseClient;
    }
    

}
