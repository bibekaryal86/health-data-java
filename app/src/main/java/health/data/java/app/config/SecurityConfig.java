package health.data.java.app.config;

import health.data.java.app.util.CommonUtils;
import health.data.java.app.util.ConstantUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeHttpRequests()
        .anyRequest()
        .authenticated()
        .and()
        .csrf()
        .disable()
        .httpBasic()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return httpSecurity.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers(HttpMethod.GET, "/tests/ping");
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    UserDetails user =
        User.withDefaultPasswordEncoder()
            .username(CommonUtils.getSystemEnvProperty(ConstantUtils.BASIC_AUTH_USR, null))
            .password(
                "{noop}"
                    .concat(CommonUtils.getSystemEnvProperty(ConstantUtils.BASIC_AUTH_PWD, null)))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }
}
