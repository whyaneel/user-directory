package com.prototype.userdirectory.model

import com.prototype.userdirectory.utils.addCommaIfNeeded
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Table

@Entity
@Table(name = "user_directory")
data class UserDAO(
    @Id
    @Column(name = "id")
    val id: String = "",

    @Column(name = "first_name")
    val firstName: String = "",

    @Column(name = "last_name")
    val lastName: String = "",

    @Column(name = "contact_number")
    val contactNumber: String = "",

    @ElementCollection
    @CollectionTable(name = "address_directory", joinColumns = [JoinColumn(name = "user_id")])
    var addresses: List<AddressDAO>? = emptyList()
) {
    fun formatAddress() = addresses?.map { (line1, line2, city, country, postal) ->
        line1.addCommaIfNeeded() + line2.addCommaIfNeeded() + city.addCommaIfNeeded() + country + " " + postal
    }
}

