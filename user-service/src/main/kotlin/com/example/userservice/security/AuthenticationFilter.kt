package com.example.userservice.security

import com.example.userservice.service.UserService
import com.example.userservice.vo.RequestLogin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.time.Instant
import java.util.*


class AuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val env: Environment
): UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val creds = jacksonObjectMapper().readValue(request.inputStream, RequestLogin::class.java)
        println(creds)
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(creds.email, creds.pwd, ArrayList())
        )

    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val userName = authResult!!.name
        val user = userService.getUserDetailsByEmail(userName)

        val secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret")!!.toByteArray())

        val secretKey = Keys.hmacShaKeyFor(secretKeyBytes)

        val now = Instant.now()

        val token: String = Jwts.builder()
            .setSubject(user.userId)
            .setExpiration(Date.from(now.plusMillis(env.getProperty("token.expiration_time")!!.toLong())))
            .setIssuedAt(Date.from(now))
            .signWith(secretKey)
            .compact()

        response?.addHeader("token", token)
        response?.addHeader("userId", user.userId)
        println(response?.getHeaders("token"))
        println(response?.getHeader("userId"))
    }

}