# Pragyan - Raspberry Pi Robotics Control App 🤖

An Android application built with Kotlin and Jetpack Compose for controlling Raspberry Pi-based robotics projects. The app features automatic network discovery to detect and connect to your Raspberry Pi, sending HTTP commands for seamless robot control.

## ✨ Features

- **🔍 Automatic IP Detection**: Automatically discovers Raspberry Pi on the local network
- **📡 Manual IP Override**: Option to manually specify Raspberry Pi IP address
- **🎮 Robot Control Interface**: Intuitive UI for sending control commands
- **📹 Video Streaming Support**: Real-time video feed from Raspberry Pi camera
- **🔌 MQTT Support**: Alternative communication protocol using MQTT
- **🎨 Modern UI**: Built with Jetpack Compose and Material Design 3
- **⚡ Real-time Communication**: Fast HTTP-based command transmission
- **🌐 Network Auto-Configuration**: Calculates Pi's IP based on network configuration

## 📱 Screenshots

<!-- Add your app screenshots here -->

## 🛠️ Tech Stack

### Android
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt
- **Networking**: 
  - Retrofit 2.9.0 (HTTP)
  - OkHttp (HTTP Client)
  - Eclipse Paho (MQTT)
- **Video Streaming**: AndroidX Media3 ExoPlayer
- **Navigation**: Jetpack Navigation Compose
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)

### Raspberry Pi Requirements
- HTTP server running on port **5001**
- Endpoint: `/command` (POST)
- Optional: MQTT broker for alternative communication
- Optional: Video streaming server for camera feed

## 📋 Prerequisites

- Android Studio Hedgehog or newer
- Android device or emulator (API 24+)
- Raspberry Pi with:
  - Python HTTP server or similar running on port 5001
  - Network connectivity (WiFi recommended)
  - Optional: Camera module for video streaming

## 🚀 Getting Started

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/masteranany23/pragyan.git
   cd pragyan
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle dependencies
   - Wait for the build to complete

4. **Run the app**
   - Connect your Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

### Raspberry Pi Setup

1. **Install Python dependencies on your Pi**
   ```bash
   pip install flask
   ```

2. **Create a simple Flask server** (example)
   ```python
   from flask import Flask, request
   
   app = Flask(__name__)
   
   @app.route('/command', methods=['POST'])
   def command():
       data = request.get_json()
       command = data.get('command')
       # Process your robot commands here
       print(f"Received command: {command}")
       return '', 200
   
   if __name__ == '__main__':
       app.run(host='0.0.0.0', port=5001)
   ```

3. **Run the server**
   ```bash
   python server.py
   ```

## 🔧 Configuration

### Network Configuration

The app automatically detects your Raspberry Pi using network calculation:
- Assumes Pi's last octet is **236** (configurable in `DynamicBaseUrlProvider.kt`)
- Default fallback IP: `192.168.151.236`
- Port: **5001**

To modify these settings, edit `DynamicBaseUrlProvider.kt`:
```kotlin
private val PI_LAST_OCTET = 236  // Change this to your Pi's last octet
private val DEFAULT_IP = "192.168.151.236"  // Fallback IP
private val PORT = 5001  // Server port
```

### Permissions

The app requires the following permissions (already configured):
- `INTERNET` - Network communication
- `ACCESS_NETWORK_STATE` - Network state monitoring
- `ACCESS_WIFI_STATE` - WiFi information
- `WAKE_LOCK` - MQTT service operation

## 📦 Project Structure

```
app/src/main/java/com/example/pragyan/
├── di/                          # Dependency Injection
│   └── AppModule.kt            # Hilt modules
├── network/                     # Network layer
│   ├── RobotApi.kt             # Retrofit API interface
│   ├── DynamicBaseUrlProvider.kt # Dynamic IP management
│   ├── MqttHelper.kt           # MQTT client
│   └── Utilities.kt            # Network utilities
├── ui/                          # UI components
│   ├── components/             # Reusable UI components
│   ├── screens/                # App screens
│   │   ├── MainScreen.kt       # Main control screen
│   │   ├── ModeScreen.kt       # Mode selection
│   │   └── VideoStreamScreen.kt # Video streaming
│   └── theme/                  # Theme configuration
├── viewmodel/                   # ViewModels
│   └── RobotViewModel.kt       # Main view model
├── MainActivity.kt              # Entry point
├── Navigation.kt                # Navigation setup
└── PragyanApplication.kt        # Application class
```

## 🎯 Usage

1. **Launch the app** on your Android device
2. **Automatic Connection**: The app will automatically detect your Raspberry Pi's IP address
3. **Manual Override** (if needed):
   - Toggle "Manual IP" switch
   - Enter your Pi's IP address
4. **Control Your Robot**: Use the control interface to send commands
5. **Video Streaming**: Tap "Start Stream" to view camera feed

## 🔌 API Communication

### HTTP Command Format

The app sends POST requests to `http://<PI_IP>:5001/command`:

```json
{
  "command": "forward"
}
```

Supported command examples:
- `forward`
- `backward`
- `left`
- `right`
- `stop`
- Custom commands as per your implementation

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Anany**
- GitHub: [@masteranany23](https://github.com/masteranany23)

## 🙏 Acknowledgments

- Built with [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Network discovery utilities
- Eclipse Paho for MQTT support
- AndroidX Media3 for video streaming

## 📞 Support

If you have any questions or issues, please open an issue on GitHub.

---

**Note**: This app is designed for educational and hobbyist robotics projects. Ensure your Raspberry Pi server is properly secured when exposed on a network.