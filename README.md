# 📞 Call Logger – Android + PHP Backend

A real-time call logging Android app that captures call events (incoming, outgoing, missed) and sends the data as JSON to a PHP backend, which stores it in a MySQL database and displays logs in a simple frontend dashboard.


## 🔧 Project Structure

```

Call-Logger/
├── app/                # Android app (Kotlin, Jetpack)
├── backend/            # PHP backend + MySQL table
├── build.gradle.kts    # Root build file (Kotlin DSL)
├── settings.gradle.kts
└── README.md

````

---

## 📱 Android App Features

- Monitors all call types: Incoming, Outgoing, Missed
- Captures and sends:
  - 📅 Date
  - ⏰ Time
  - 📞 Call Type
  - ☎️ Device Number
  - 📲 Client Number
  - ⏱️ Ring Duration
  - 📞 Call Duration
- Sends data as JSON using OkHttp to PHP endpoint

---

## 📦 Backend Features

- Built with PHP (uses `log_call.php` for receiving JSON)
- Stores call data in MySQL using `calls` table
- Displays logs via a basic HTML + PHP table

---

## 🛠️ Setup Instructions

### ⚙️ PHP + MySQL Setup

1. **Install PHP** (or use XAMPP/WAMP):
   ```bash
   php -S 127.0.0.1:8000
````

2. **Import SQL Table**

   ```sql
   CREATE TABLE calls (
       id             BIGINT AUTO_INCREMENT PRIMARY KEY,
       date           DATE        NOT NULL,
       time           TIME        NOT NULL,
       type           ENUM('Incoming','Outgoing','Missed') NOT NULL,
       device_number  VARCHAR(20) NOT NULL,
       client_number  VARCHAR(20) NOT NULL,
       ring_duration  INT         NOT NULL,
       call_duration  INT         NOT NULL,
       created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

3. **Run backend server:**

   ```bash
   cd backend/
   php -S 127.0.0.1:8000
   ```

---

### 🤖 Android App Setup

1. Open the project in **Android Studio**

2. Add required permissions to `AndroidManifest.xml`:

   ```xml
   <uses-permission android:name="android.permission.READ_PHONE_STATE" />
   <uses-permission android:name="android.permission.READ_CALL_LOG" />
   <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
   ```

3. Replace backend URL in `sendCallLogToServer()`:

   ```kotlin
   .url("http://10.0.2.2:8000/log_call.php") // For emulator
   // OR
   .url("http://<your-ip>:8000/log_call.php") // For physical device
   ```

4. Run the app on a device or emulator and make calls to test.

---

## 🛡️ Permissions & API Compatibility

| Permission           | Required for           |
| -------------------- | ---------------------- |
| `READ_PHONE_STATE`   | Detect call state      |
| `READ_CALL_LOG`      | Access number/duration |
| `FOREGROUND_SERVICE` | Run background service |

> Make sure to request runtime permissions for Android 6.0+.

---

## 📊 Sample JSON Sent

```json
{
  "date": "2025-06-15",
  "time": "17:45:12",
  "type": "Incoming",
  "device_number": "+911234567890",
  "client_number": "+919876543210",
  "ring_duration": 5,
  "call_duration": 60
}
```

---

## 🧠 Technologies Used

* **Android**: Kotlin, Jetpack, OkHttp, Gson
* **Backend**: PHP 8.x, MySQL
* **Frontend**: Basic HTML + PHP

---

## 📁 License

MIT License. Free to use, modify, and distribute.

---

## 🙋‍♂️ Author

**Nikhil Dev R**
📬 [View Profile](https://github.com/Nikhil-Dev-R)

---

## 📌 Future Plans

* User authentication
* Firebase integration
* Enhanced frontend (charts, filters)
* Remote device sync

```
