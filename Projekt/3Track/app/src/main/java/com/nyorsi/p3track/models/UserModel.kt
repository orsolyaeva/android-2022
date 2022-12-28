package com.nyorsi.p3track.models

import androidx.lifecycle.MutableLiveData

enum class UserType {
    HR_MANAGER,
    DEPARTMENT_LEAD,
    SIMPLE_EMPLOYEE
}

data class UserModel (
    var id: Int,
    var lastName: String,
    var firstName: String,
    var email: String,
    var type: UserType,
    var location: String?,
    var phoneNumber: String?,
    var departmentId: Int,
    var image: String?
)
