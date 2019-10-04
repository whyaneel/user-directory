package com.prototype.userdirectory.service

import com.prototype.userdirectory.NotFoundException
import com.prototype.userdirectory.dto.AddressDTO
import com.prototype.userdirectory.dto.UserDTO
import com.prototype.userdirectory.model.UserDAO
import com.prototype.userdirectory.repository.UserRepository
import com.prototype.userdirectory.utils.fromDTO
import com.prototype.userdirectory.utils.toDTO
import org.springframework.stereotype.Service

@Service
class UserDirectoryService(
    private val userRepository: UserRepository
) {
    fun getAllUsers(): List<UserDTO> = userRepository.findAll().map { it.toDTO() }

    fun getUser(id: String): UserDTO = userOrThrow(id).toDTO()

    fun postUser(user: UserDTO): UserDTO {
        val userDAO = user.fromDTO()
        return userRepository.save(userDAO).toDTO()
    }

    fun updateAddress(id: String, address: AddressDTO): UserDTO {
        val userDAO = userOrThrow(id)
        userDAO.apply {
            addresses = addresses?.plus(address.fromDTO())
        }
        return userRepository.save(userDAO).toDTO()
    }

    fun deleteUser(id: String) = userRepository.delete(userOrThrow(id))

    private fun userOrThrow(id: String): UserDAO {
        val userDAO = userRepository.findById(id)
            .orElseThrow { NotFoundException("No User Profile found [$id]") }
        return userDAO
    }

    //TODO Unit Test
}