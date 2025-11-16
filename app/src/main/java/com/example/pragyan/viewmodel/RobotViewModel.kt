package com.example.pragyan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pragyan.di.DynamicApiFactory
import com.example.pragyan.network.DynamicBaseUrlProvider
import com.example.pragyan.network.RobotApi
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RobotViewModel @Inject constructor(
    private val apiFactory: DynamicApiFactory,
    val baseUrlProvider: DynamicBaseUrlProvider
) : ViewModel() {

    // Private reference to the API that gets updated when the base URL changes
    private val api: RobotApi
        get() = apiFactory.create(RobotApi::class.java)

    // StateFlow to expose the current IP address to the UI
    private val _currentIpAddress = MutableStateFlow("")
    val currentIpAddress: StateFlow<String> = _currentIpAddress

    // Initialize with the current base URL
    init {
        updateCurrentIpAddress()

        // Observe changes to the base URL
        viewModelScope.launch {
            baseUrlProvider.baseUrlFlow.collect { newBaseUrl ->
                _currentIpAddress.value = newBaseUrl.removePrefix("http://").removeSuffix(":5001/")
            }
        }
    }

    /**
     * Update the current IP address from the base URL provider
     */
    private fun updateCurrentIpAddress() {
        val url = baseUrlProvider.getCurrentBaseUrl()
        _currentIpAddress.value = url.removePrefix("http://").removeSuffix(":5001/")
    }

    /**
     * Set whether to use manual IP and what IP to use
     */
    fun setManualIp(useManual: Boolean, ipAddress: String = "") {
        baseUrlProvider.setManualIp(useManual, ipAddress)
        // No need to call updateCurrentIpAddress() here as we're now collecting the flow
    }

    /**
     * Send command to the robot
     */
    fun sendMovementCommand(direction: String) {
        viewModelScope.launch {
            try {
                api.sendCommand(CommandBody(
                    type = "instant",  // Add type parameter
                    command = direction
                ))
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    fun sendCommand(command: String) {
        viewModelScope.launch {
            try {
                // Get a fresh API instance with the current base URL
                api.sendCommand(CommandBody(type="feature",command=command))
            } catch (e: Exception) {
                // Handle network errors
            }
        }
    }
    fun sendFeature(feature: String) {
        viewModelScope.launch {
            try {
                // Get a fresh API instance with the current base URL
                api.sendCommand(CommandBody(type="feature",feature=feature))
            } catch (e: Exception) {
                // Handle network errors
            }
        }
    }
}
data class CommandBody(
    @SerializedName("type") val type: String,  // Add this
    @SerializedName("command") val command: String? = null,
    @SerializedName("feature") val feature: String? = null
)