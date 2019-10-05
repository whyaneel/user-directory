package com.prototype.userdirectory.repository

import com.prototype.userdirectory.dto.UserDTO
import com.prototype.userdirectory.model.UserDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<UserDAO, String> {
    @Query(
        value = "SELECT * FROM user_directory WHERE is_deleted = true",
        nativeQuery = true
    )
    fun findAllInActive(): List<UserDAO>

    @Query(
        value = "SELECT * FROM user_directory WHERE is_deleted = true AND id = (:id)",
        nativeQuery = true
    )
    fun findInActiveById(@Param("id") id: String): Optional<UserDAO>

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM user_directory WHERE is_deleted = true AND id = (:id)",
        nativeQuery = true
    )
    fun permDelete(@Param("id") id: String)
}