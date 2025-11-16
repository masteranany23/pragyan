package com.example.pragyan.network

import android.content.Context
import com.example.pragyan.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class responsible for dynamically providing the base URL for Retrofit
 * Now supports manual IP override
 */
@Singleton
class DynamicBaseUrlProvider @Inject constructor(
    private val context: Context
) {
    // Pi's last octet is fixed
    private val PI_LAST_OCTET = 236

    // Default fallback IP if network calculation fails
    private val DEFAULT_IP = "192.168.151.236"

    // Port where the Pi's service is running
    private val PORT = 5001

    // Flag to indicate if manual IP should be used
    private var useManualIp = false

    // Manual IP address value
    private var manualIpAddress = ""

    // StateFlow to notify subscribers when the base URL changes
    private val _baseUrlFlow = MutableStateFlow(calculateBaseUrl())
    val baseUrlFlow: StateFlow<String> = _baseUrlFlow

    /**
     * Get the current base URL for the API
     */
    fun getCurrentBaseUrl(): String {
        return if (useManualIp && manualIpAddress.isNotEmpty()) {
            "http://$manualIpAddress:$PORT/"
        } else {
            calculateBaseUrl()
        }
    }

    /**
     * Calculate the base URL using network utils
     */
    private fun calculateBaseUrl(): String {
        val ipAddress = NetworkUtils.computePiIp(context, PI_LAST_OCTET) ?: DEFAULT_IP
        return "http://$ipAddress:$PORT/"
    }

    /**
     * Set manual IP address
     * @param useManual Whether to use manually entered IP
     * @param ipAddress The manually entered IP address
     */
    fun setManualIp(useManual: Boolean, ipAddress: String = "") {
        this.useManualIp = useManual
        if (useManual && ipAddress.isNotEmpty()) {
            this.manualIpAddress = ipAddress
        }
        // Notify subscribers that the base URL has changed
        _baseUrlFlow.value = getCurrentBaseUrl()
    }
}