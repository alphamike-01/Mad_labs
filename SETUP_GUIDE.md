# Android Studio Practical Setup & Environment Guide

This guide provides step-by-step instructions for importing, building, running, and debugging the practical projects in Android Studio.

---

## 1. System Requirements

- **Operating System**: Windows 10/11 (64-bit), macOS (Intel or Apple Silicon), or Linux
- **RAM**: Minimum 8 GB (16 GB recommended)
- **Disk Space**: At least 10 GB free space for Android Studio, Android SDK, and emulators
- **JDK**: Java Development Kit 17 or 21 (bundled automatically with Android Studio as JetBrains Runtime - JBR)
- **Android Studio**: Android Studio Ladybug (2024.2.1), Koala, Meerkat, or later

---

## 2. Importing a Practical Project

Every practical folder in this repository is an **independent Android Studio project**.

### Step-by-Step Import:
1. Extract `Mobile_Application_Development_Java_Practicals.zip` to a convenient location on your drive (e.g. `C:\AndroidProjects\` or `~/AndroidProjects/`). Avoid paths with special symbols or excessively long folder names.
2. Open **Android Studio**.
3. In the welcome dialog, click **Open**, or from the menu select **File -> Open...**.
4. Browse to the extracted directory and select the specific practical you want to run (e.g. `Practical_03_UI_Design`).
5. Click **OK**.
6. Wait for Android Studio to index files and complete the Gradle sync. The first sync may take 1-3 minutes as Gradle resolves AndroidX dependencies.

---

## 3. Configuring the Android Virtual Device (AVD / Emulator)

To run applications without a physical phone:
1. In Android Studio, open the **Device Manager** (**Tools -> Device Manager** or click the phone icon on the top toolbar).
2. Click **Create Virtual Device**.
3. Select a phone hardware definition (e.g., **Pixel 7** or **Pixel 8**). Click **Next**.
4. Under the **Recommended** tab, select a system image:
   - Recommended: **API 34 (UpsideDownCake)** or **API 35 (VanillaIceCream)**.
5. Click **Download** if the system image is not already installed, then click **Next**.
6. Verify device orientation and click **Finish**.
7. Launch the emulator by clicking the green **Play** triangle in the Device Manager.

---

## 4. Running on a Physical Android Device

1. On your Android phone, enable **Developer Options**:
   - Go to **Settings -> About Phone**.
   - Tap **Build Number** 7 times until you see the message *"You are now a developer!"*.
2. Go to **Settings -> System -> Developer Options** and enable **USB Debugging**.
3. Connect your phone to your computer using a USB cable.
4. When prompted on your phone screen, check *"Always allow from this computer"* and tap **Allow**.
5. In Android Studio's target device dropdown (top toolbar), select your physical phone.
6. Click **Run 'app'** (`Shift + F10`).

---

## 5. Resolving Common Gradle Synchronization Issues

### Issue 1: "SDK Location not found"
- **Cause**: Android Studio does not know where your Android SDK is installed.
- **Fix**: Create a file named `local.properties` in the practical project root and specify your SDK path:
  ```properties
  # Windows example:
  sdk.dir=C\:\\Users\\<YourUsername>\\AppData\\Local\\Android\\Sdk

  # macOS example:
  sdk.dir=/Users/<YourUsername>/Library/Android/sdk

  # Linux example:
  sdk.dir=/home/<YourUsername>/Android/Sdk
  ```

### Issue 2: "Unsupported Java version"
- **Cause**: Android Gradle Plugin 8.5+ requires JDK 17 or higher.
- **Fix**: Go to **File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle**.
  - Under **Gradle JDK**, select **Embedded JDK (version 17 or 21)** or browse to your installed JDK 17/21.

### Issue 3: Gradle offline mode or proxy errors
- **Cause**: In college laboratories behind proxy servers, Gradle may fail to reach Maven repositories.
- **Fix**: Configure proxy in **File -> Settings -> Appearance & Behavior -> System Settings -> HTTP Proxy**, or toggle **Offline Mode** in the Gradle tool window if dependencies are already cached.
