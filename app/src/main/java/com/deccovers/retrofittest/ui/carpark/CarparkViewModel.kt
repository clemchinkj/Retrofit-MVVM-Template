package com.deccovers.retrofittest.ui.carpark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deccovers.retrofittest.data.ContentRepository
import com.deccovers.retrofittest.data.carpark.model.CarparkResponse
import com.deccovers.retrofittest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarparkViewModel @Inject constructor(
    private val contentRepository: ContentRepository
): ViewModel() {

    sealed class CarparkEvent {
        class Success(val carparkResponse: CarparkResponse): CarparkEvent()
        class Failure(val errorText: String): CarparkEvent()
        object Loading : CarparkEvent()
        object Empty : CarparkEvent()
    }

    private val _carparkRetrieval = MutableStateFlow<CarparkEvent>(CarparkEvent.Empty)
    val carparkRetrieval: StateFlow<CarparkEvent> = _carparkRetrieval

    fun getCarparkResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            _carparkRetrieval.value = CarparkEvent.Loading

            when(val carparkResponse = contentRepository.getCarparkResponse()) {
                is Resource.Failure -> {
                    Log.e("getCarparkResponse()", "getCarparkResponse: Resource.Failure: ${carparkResponse.message}")
                    _carparkRetrieval.value = CarparkEvent.Failure(carparkResponse.message!!)
                }

                is Resource.Success -> {
                    Log.d("getCarparkResponse()", "getCarparkResponse: Resource.Success")
                    _carparkRetrieval.value = CarparkEvent.Success(carparkResponse.data!!)
                }
            }
        }
    }

}