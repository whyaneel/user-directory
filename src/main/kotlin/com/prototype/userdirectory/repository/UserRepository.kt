package com.prototype.userdirectory.repository

import com.prototype.userdirectory.model.UserDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserDAO, String>