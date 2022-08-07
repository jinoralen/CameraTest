package com.jinoralen.cameratest.feature.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.jinoralen.cameratest.domain.StoreRepository
import com.jinoralen.cameratest.domain.UploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val uploadRepository: UploadRepository,
    private val storeRepository: StoreRepository,
): ViewModel() {

    private val _eventFlow = Channel<UploadViewModelEvent>(Channel.BUFFERED)
    val eventFlow: Flow<UploadViewModelEvent> = _eventFlow.receiveAsFlow()

    fun uploadImage(image: File) {
        viewModelScope.launch {
            listOf(
                async {
                    when(val result = uploadRepository.upload(image)) {
                        is Either.Left -> _eventFlow.send(UploadViewModelEvent.Error(result.value.error))
                        is Either.Right -> _eventFlow.send(UploadViewModelEvent.UploadSuccessful)
                    }
                },
                async {
                    when(val result =  storeRepository.storeImage(image)) {
                        is Either.Left -> _eventFlow.send(UploadViewModelEvent.Error(result.value.error))
                        is Either.Right -> _eventFlow.send(UploadViewModelEvent.StoreSuccessful)
                    }
                }
            ).awaitAll()

            _eventFlow.send(UploadViewModelEvent.Success)
        }
    }

    sealed class UploadViewModelEvent {
        object UploadSuccessful: UploadViewModelEvent()
        object StoreSuccessful: UploadViewModelEvent()
        object Success: UploadViewModelEvent()

        data class Error(val error: String): UploadViewModelEvent()
    }
}
