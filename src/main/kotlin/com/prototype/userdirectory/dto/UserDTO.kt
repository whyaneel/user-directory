package com.prototype.userdirectory.dto

import com.prototype.userdirectory.api.validator.ValidUserId
import com.prototype.userdirectory.model.AddressDAO
import com.prototype.userdirectory.model.UserDAO
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserDTO(
    @field:ValidUserId
    val id: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val contactNumber: String? = null,
    val addresses: List<String>? = null
)
