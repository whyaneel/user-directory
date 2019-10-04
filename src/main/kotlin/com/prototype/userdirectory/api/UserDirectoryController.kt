package com.prototype.userdirectory.api

import com.prototype.userdirectory.api.validator.ValidUserId
import com.prototype.userdirectory.dto.AddressDTO
import com.prototype.userdirectory.dto.UserDTO
import com.prototype.userdirectory.service.UserDirectoryService
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
@Validated
class UserDirectoryController(
    private val userDirectoryService: UserDirectoryService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/users")
    fun getUsers(): List<UserDTO> = userDirectoryService.getAllUsers()

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable @ValidUserId id: String) = userDirectoryService.getUser(id)

    @PostMapping("/users")
    fun postUser(@RequestBody @Valid user: UserDTO): UserDTO {
        logger.info("UserDTO : {}", user)
        val response = userDirectoryService.postUser(user)
        logger.info("User Created/ Updated : {}", response)
        return response
    }

    @PostMapping("/users/{id}/address")
    fun postAddress(@PathVariable @ValidUserId id: String, @RequestBody @Valid address: AddressDTO): UserDTO {
        logger.info("({}) AddressDTO : {}", id, address)
        val response = userDirectoryService.updateAddress(id, address)
        logger.info("User Address Updated : {}", response)
        return response
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable @ValidUserId id: String) = userDirectoryService.deleteUser(id)

    //TODO Soft Delete
    //TODO Search Users with (FirstName, LastName, country), Sort By FirstName, Pagination (10 items)
}