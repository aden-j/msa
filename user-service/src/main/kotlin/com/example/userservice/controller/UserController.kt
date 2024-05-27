package com.example.userservice.controller

import com.example.userservice.dto.UserDto
import com.example.userservice.service.UserService
import com.example.userservice.vo.RequestUser
import com.example.userservice.vo.ResponseUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/")
class UserController(
    @Autowired val env: Environment,
    @Autowired val userService: UserService
) {

    @GetMapping("/health-check")
    fun status(): String {
        return """It's working in User Service,
         port(local.server.port)= ${env.getProperty("local.server.port")},
         port(server.port)= ${env.getProperty("server.port")},
         token secret= ${env.getProperty("token.secret")},
         token expiration time= ${env.getProperty("token.expiration_time")}"""
    }

    @GetMapping("/welcome")
    fun welcome(
        @Value("\${greeting.message}") message: String
    ): String {
        return message
    }

    @PostMapping("/users")
    fun createUser(
        @RequestBody user: RequestUser
    ): ResponseEntity<ResponseUser> {

        val responseUser = userService.createUser(
            UserDto(
                email = user.email,
                name = user.name,
                pwd = user.pwd,
            )
        ).toDto()
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser)
    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<ResponseUser>> {
        val userList = userService.getUserAll()
        val result = mutableListOf<ResponseUser>()
        userList.forEach { it ->
            result.add(it.toDto())
        }
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("/users/{userId}")
    fun getUsers(
        @PathVariable userId: String
    ): ResponseEntity<ResponseUser> {
        val user = userService.getUserByUserId(userId)
        return ResponseEntity.status(HttpStatus.OK).body(user.toDto())
    }
}


fun UserDto.toDto(): ResponseUser {
    return ResponseUser(
        email = this.email,
        name = this.name,
        userId = this.userId,
        orders = emptyList(),
    )
}