package com.example.pragyan.network

import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MqttHelper(
    context: Context,
    brokerUrl: String = "tcp://192.168.x.236:1883",
    clientId: String = MqttClient.generateClientId()
) {
    private val client = MqttAndroidClient(context, brokerUrl, clientId)

    init {
        client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) { /* handle loss */ }
            override fun messageArrived(topic: String, message: MqttMessage) {
                // If you ever need Pi→App, handle messages here
            }
            override fun deliveryComplete(token: IMqttDeliveryToken) {}
        })
        client.connect().apply {
            actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // subscribe if needed, e.g. to "robot/status/#"
                    client.subscribe("robot/status/#", 1)
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) { /* retry */ }
            }
        }
    }

    fun publishCommand(cmd: String) {
        val msg = MqttMessage(cmd.toByteArray())
        msg.qos = 1
        client.publish("robot/commands", msg)
    }
}
