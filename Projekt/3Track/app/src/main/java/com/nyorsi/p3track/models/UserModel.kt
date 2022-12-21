package com.nyorsi.p3track.models

import androidx.lifecycle.MutableLiveData

data class UserModel (
    var id: Int,
    var lastName: String,
    var firstName: String,
    var email: String,
    var type: Int,
    var location: String?,
    var phoneNumber: String?,
    var departmentId: Int,
    var image: String?
)
