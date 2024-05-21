package com.widi.scan.ui.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widi.scan.data.Result
import com.widi.scan.data.UserRepository
import com.widi.scan.data.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(
    private val storyRepository: UserRepository
):ViewModel() {
    private val _responseResult = MutableLiveData<Result<RegisterResponse>>()
    val responseResult = _responseResult

    fun submitRegister(name:String,email:String, password:String){
        viewModelScope.launch {
            try {
                _responseResult.value = Result.Loading
                val response = storyRepository.register(name,email,password)
                if (!response.error!!){
                    _responseResult.value = Result.Success(response)
                }
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                _responseResult.value = Result.Error(errorBody?:e.message())
            }
        }
    }
}
