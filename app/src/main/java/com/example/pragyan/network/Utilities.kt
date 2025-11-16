package com.example.pragyan.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import java.math.BigInteger
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.nio.ByteOrder
import java.util.Enumeration

object NetworkUtils {
    /**
     * Gets the device's current IP address on the WiFi network
     */
    fun getDeviceIpAddress(context: Context): String? {
        // Modern approach
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return null
            val linkProperties = connectivityManager.getLinkProperties(activeNetwork) ?: return null

            // Get addresses from link properties
            for (address in linkProperties.linkAddresses) {
                val inetAddress = address.address
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return inetAddress.hostAddress
                }
            }
        }

        // Fallback to getting all network interfaces
        try {
            val networkInterfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val addresses: Enumeration<InetAddress> = networkInterface.inetAddresses

                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address is Inet4Address) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Legacy fallback using WifiManager
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            var ipAddress = wifiManager.connectionInfo.ipAddress

            // Convert little-endian to big-endian if needed
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                ipAddress = Integer.reverseBytes(ipAddress)
            }

            // Convert to InetAddress
            val ipByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
            return InetAddress.getByAddress(ipByteArray).hostAddress
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Computes the Pi's IP address based on the device's IP and the Pi's known last octet
     */
    fun computePiIp(context: Context, lastOctet: Int): String? {
        val deviceIp = getDeviceIpAddress(context) ?: return null

        // Split the IP address into octets
        val octets = deviceIp.split(".")
        if (octets.size != 4) return null

        // Replace the last octet with the Pi's octet
        return "${octets[0]}.${octets[1]}.${octets[2]}.$lastOctet"
    }
}