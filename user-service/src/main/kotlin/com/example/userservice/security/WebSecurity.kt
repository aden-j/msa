package com.example.userservice.security

import com.example.userservice.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.IpAddressMatcher
import java.util.function.Supplier


@Configuration
@EnableWebSecurity
class WebSecurity(
    private val userService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val env: Environment
) {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        // Configure AuthenticationManagerBuilder
        val authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder)

        val authenticationManager = authenticationManagerBuilder.build()

        http.csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }

        //        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
            Customizer { authz ->
                authz
                    .requestMatchers(AntPathRequestMatcher("/actuator/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/user-service/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/health-check/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/welcome/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/user-service/health-check/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/h2-console/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/users", "POST")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/users", "GET")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/h2-console/**"))
                    .permitAll() //                        .requestMatchers("/**").access(this::hasIpAddress)
                    .requestMatchers("/**").access(
                        WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1') or hasIpAddress('172.30.1.48')")
                    )
                    .anyRequest().authenticated()
            }
        )
            .authenticationManager(authenticationManager)
            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity?> ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        http.addFilter(getAuthenticationFilter(authenticationManager))
        http.headers { headers: HeadersConfigurer<HttpSecurity?> ->
            headers.frameOptions { frameOptions -> frameOptions.disable() }
        }

        return http.build()
    }

    private fun hasIpAddress(
        authentication: Supplier<Authentication>,
        `object`: RequestAuthorizationContext
    ): AuthorizationDecision {
        return AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(`object`.request))
    }

    @Throws(Exception::class)
    private fun getAuthenticationFilter(authenticationManager: AuthenticationManager): AuthenticationFilter {
        return AuthenticationFilter(authenticationManager, userService, env)
    }

    companion object {
        const val ALLOWED_IP_ADDRESS: String = "127.0.0.1"
        const val SUBNET: String = "/32"
        val ALLOWED_IP_ADDRESS_MATCHER: IpAddressMatcher = IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET)
    }
}