package com.nyorsi.p3track.viewModels

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.models.DepartmentModel
import com.nyorsi.p3track.repositories.DepartmentRepository
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch

class DepartmentViewModel(application: Application): AndroidViewModel(application) {
    private val departmentRepository = DepartmentRepository()
    val getDepartmentsState: MutableLiveData<RequestState> = MutableLiveData()
    var departmentList: MutableLiveData<List<DepartmentModel>> = MutableLiveData()

    fun getDepartments() {
        getDepartmentsState.value = RequestState.LOADING
        viewModelScope.launch {
            try {
                val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                val token = sharedPref.getString("token", null)
                if(token != null) {
                    departmentList.value = listOf()
                    val response = departmentRepository.getDepartments(token)
                    if(response.isSuccessful) {
                        val departmentListResponse = response.body()!!
                        for (department in departmentListResponse) {
                            departmentList.value = departmentList.value?.plus(DepartmentModel(department.ID, department.name))
                        }
                        getDepartmentsState.value = RequestState.SUCCESS
                    } else {
                        getDepartmentsState.value = RequestState.UNKNOWN_ERROR
                    }
                }
            } catch (e: Exception) {
                getDepartmentsState.value = RequestState.UNKNOWN_ERROR
            }
        }
    }

    fun getDepartmentWithId(id: Int): DepartmentModel {
        val result = departmentList.value?.find { it.id == id }
        if (result != null) {
            return result
        }
        throw Exception("Department not found")
    }
}