# 🏠 Room Rental Finder App - TimTro App

## 📱 Introduction

**TimTro App** is an Android mobile application that helps users search for and post rental listings for boarding houses and rooms easily and conveniently. The application is developed in Java with Firebase as the backend and integrates many modern features.

## ✨ Key Features

### 🔐 Authentication and Account Management
- **Registration/Login**: Authentication system via Firebase Authentication
- **Forgot Password**: Send password reset email
- **Profile Management**: Update personal information
- **Email Verification**: Account security

### 🏠 Post Management
- **Post Listings**: Post rental room listings with complete information
- **Upload Images**: Upload room images (Base64 encoding)
- **Detailed Information**: Price, area, address, description, target tenants
- **Expiration Management**: Posts have validity periods
- **Post Approval**: System reviews posts before displaying

### 🔍 Smart Search
- **Location-based Search**: Filter by province/city, district
- **Text Search**: Search by keywords
- **Advanced Filters**: Filter by price, area, room type
- **List Display**: ListView interface with custom adapters

### 🗺️ Map Integration
- **Google Maps**: Display room locations on map
- **GPS Positioning**: Save and display coordinates (latitude, longitude)
- **Directions**: Integrate Google Maps for navigation to locations
- **Multiple Maps Activities**: Multiple map screens for different purposes

### 💳 Payment System
- **PayPal Integration**: Online payment via PayPal
- **Cash Payment**: Direct payment option
- **Fee Management**: Calculate posting fees by listing type and duration
- **PayPal WebView**: In-app payment interface

### 👥 Admin System
- **Admin Panel**: Separate administrative interface
- **User Management**: View, edit, delete user accounts
- **Post Management**: Approve, reject, delete posts
- **Analytics**: Monitor app activity

## 🛠️ Technologies Used

### Frontend (Android)
- **Language**: Java
- **SDK**: Android API 24+ (Android 7.0+)
- **UI Framework**: 
  - Material Design Components
  - Navigation Component
  - RecyclerView, CardView
  - Bottom Navigation

### Backend & Database
- **Firebase Realtime Database**: Real-time data storage
- **Firebase Storage**: Image storage
- **Firebase Authentication**: User authentication

## 📱 App Structure

### Main Activities
- `MainActivity`: Main activity with Bottom Navigation
- `LoginActivity`: Login screen
- `PostActivity`: New post creation
- `DetailActivity`: Post details
- `MapsActivity`: Map display
- `AdminActivity`: Admin panel

### Fragments
- `HomeFragment`: Home page - post listings
- `SearchFragment`: Search and filtering
- `PostFragment`: Post creation form
- `LoginFragment`: Login form

## 📥 DETAILED DOWNLOAD AND INSTALLATION GUIDE

### 🛠️ Development Environment Setup

#### 1. Download and Install Android Studio
```bash
# Step 1: Download Android Studio
1. Visit: https://developer.android.com/studio
2. Click "Download Android Studio"
3. Choose appropriate version:
   - Windows: android-studio-2023.1.1.28-windows.exe
   - macOS: android-studio-2023.1.1.28-mac.dmg  
   - Linux: android-studio-2023.1.1.28-linux.tar.gz

# Step 2: Installation
- Windows: Run .exe file and follow instructions
- macOS: Drag Android Studio to Applications
- Linux: Extract and run studio.sh
```

#### 2. Install Java Development Kit (JDK)
```bash
# Check if Java is installed
java -version
javac -version

# If not installed, download JDK 11:
# Windows: https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe
# macOS: https://download.oracle.com/java/17/latest/jdk-17_macos-x64_bin.dmg
# Linux: sudo apt install openjdk-11-jdk

# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

#### 3. Setup Android SDK
```bash
# In Android Studio:
1. Tools → SDK Manager
2. SDK Platforms: Select API 24, 30, 35
3. SDK Tools: 
   - Android SDK Build-Tools
   - Android Emulator
   - Android SDK Platform-Tools
   - Google Play services
4. Apply → OK
```

### 📂 Download Source Code

#### Method 1: Clone from Git (Recommended)
```bash
# Install Git if not available
# Windows: https://git-scm.com/download/win
# macOS: brew install git
# Linux: sudo apt install git

# Clone repository
git clone https://github.com/your-username/ProjectLTMB4.git
cd ProjectLTMB4

# Check project structure
ls -la
# Should see folders: app/, gradle/, gradlew, settings.gradle
```

#### Method 2: Download ZIP file
```bash
1. Access GitHub repository
2. Click "Code" button → "Download ZIP"
3. Extract ZIP file
4. Rename folder to "ProjectLTMB4"
```

### 🔧 External Services Configuration

#### 🔥 Firebase - REQUIRED STEP

**Step 1: Create Firebase Project**
```bash
1. Visit: https://console.firebase.google.com
2. Click "Add project"
3. Enter project name: "TimTro-App"
4. Disable Google Analytics (optional)
5. Click "Create project"
```

**Step 2: Add Android App**
```bash
1. Click "Add app" → Android icon
2. Fill information:
   - Package name: com.example.finalapp
   - App nickname: TimTro App
   - Debug signing certificate SHA-1: (leave empty)
3. Click "Register app"
```

**Step 3: Download Configuration**
```bash
1. Download google-services.json
2. Copy file to: ProjectLTMB4/app/google-services.json
3. ⚠️ IMPORTANT: Place in this exact location
```

**Step 4: Enable Services**
```bash
# Authentication
1. Authentication → Get started
2. Sign-in method → Email/Password → Enable

# Realtime Database  
1. Realtime Database → Create Database
2. Start in test mode
3. Choose location: asia-southeast1

# Storage
1. Storage → Get started  
2. Start in test mode
3. Choose location: asia-southeast1
```

#### 🗺️ Google Maps - REQUIRED STEP

**Step 1: Create Google Cloud Project**
```bash
1. Visit: https://console.cloud.google.com
2. New Project → Enter name → Create
3. Select created project
```

**Step 2: Enable APIs**
```bash
1. APIs & Services → Library
2. Find and enable:
   - Maps SDK for Android
   - Places API
   - Geocoding API
   - Directions API
```

**Step 3: Create API Key**
```bash
1. APIs & Services → Credentials
2. Create Credentials → API Key
3. Copy API key
4. Edit API key:
   - Application restrictions: Android apps
   - Package name: com.example.finalapp
   - SHA-1: (can leave empty for development)
```

**Step 4: Update Code**
```xml
<!-- File: app/src/main/res/values/google_maps_api.xml -->
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">
        PASTE_YOUR_API_KEY_HERE
    </string>
</resources>
```

#### 💳 PayPal - OPTIONAL

```bash
1. Visit: https://developer.paypal.com
2. Log in → Create App
3. App Type: Merchant
4. Copy Client ID from Sandbox
5. Update PayPalConfig.java:
   public static final String PAYPAL_CLIENT_ID = "your_client_id_here";
```

### 🚀 Build and Run Application

#### Step 1: Open Project
```bash
1. Open Android Studio
2. File → Open
3. Select ProjectLTMB4 folder
4. Wait for Gradle sync (2-5 minutes)
```

#### Step 2: Check Dependencies
```bash
# If Gradle sync error:
1. File → Invalidate Caches → Invalidate and Restart
2. Or: ./gradlew clean build
```

#### Step 3: Run Application

**On Real Device:**
```bash
1. Enable Developer Options:
   - Settings → About Phone → Build Number (tap 7 times)
2. Enable USB Debugging:
   - Settings → Developer Options → USB Debugging
3. Connect USB → Allow Debugging
4. Android Studio: Run → Run 'app'
```

**On Emulator:**
```bash
1. Tools → AVD Manager → Create Virtual Device
2. Choose: Pixel 4 (API 30)
3. Download system image if needed
4. Finish → Start emulator
5. Run → Run 'app'
```

### ✅ Verify Successful Installation

```bash
# App runs successfully when:
1. ✅ Splash screen displays
2. ✅ Bottom navigation has 4 tabs
3. ✅ Can register new account
4. ✅ Firebase connection works
5. ✅ Google Maps displays (if configured)
6. ✅ No crashes or force close
```

### 🔧 Common Error Solutions

#### Error "google-services.json missing"
```bash
# Solution:
1. Check google-services.json file in app/
2. Package name must be correct: com.example.finalapp
3. Restart Android Studio
```

#### Error "API key not found"
```bash
# Solution:
1. Check google_maps_api.xml
2. API key must be valid and enabled
3. Package name restriction must be correct
```

#### Error "Build failed"
```bash
# Solution:
./gradlew clean
./gradlew build

# Or:
Build → Clean Project
Build → Rebuild Project
```

#### Error "Dependencies not found"
```bash
# Solution:
1. File → Sync Project with Gradle Files
2. Check internet connection
3. Check gradle version compatibility
```

---

## 🚀 Installation and Running

### System Requirements
- Android Studio Flamingo or newer
- JDK 11+
- Android SDK API 24+
- Google Play Services

### Installation Steps

1. **Clone repository**
```bash
git clone [repository-url]
cd ProjectLTMB4
```

2. **Configure Firebase**
   - Create new Firebase project at [Firebase Console](https://console.firebase.google.com)
   - Add Android app with package name: `com.example.finalapp`
   - Download `google-services.json` and place in `app/` folder
   - Enable Realtime Database, Storage and Authentication

3. **Configure Google Maps**
   - Create API key at [Google Cloud Console](https://console.cloud.google.com)
   - Enable Maps SDK for Android
   - Add API key to `app/src/main/res/values/google_maps_api.xml`

4. **Configure PayPal**
   - Register PayPal Developer account
   - Update PayPal configuration in `PayPalConfig.java`

5. **Build and run**
```bash
./gradlew assembleDebug
```

### Firebase Database Configuration
```json
{
  "DangBai": {
    "rules": {
      ".read": true,
      ".write": "auth != null"
    }
  },
  "TinhTP": {
    "rules": {
      ".read": true,
      ".write": "auth != null"
    }
  },
  "QuanHuyen": {
    "rules": {
      ".read": true,
      ".write": "auth != null"  
    }
  }
}
```

## 📖 User Guide

### For Users
1. **Register account** or login
2. **Verify email** to activate account
3. **Search for rooms** by location and criteria
4. **View details** of posts and contact landlords
5. **Post rental listings** for your rooms
6. **Pay posting fees** via PayPal or cash

### For Administrators
1. Login with admin account
2. Manage users: view, edit, delete
3. Review posts: approve or reject
4. Monitor system statistics

## 🎨 User Interface

The application uses Material Design with:
- **Bottom Navigation**: Main navigation
- **Card Layout**: Display post listings
- **Floating Action Button**: Add new posts
- **Search Interface**: Smart search
- **Map Integration**: Interactive maps

## 🔧 Advanced Configuration

### PayPal Customization
```java
// PayPalConfig.java
public static final String PAYPAL_CLIENT_ID = "your_paypal_client_id";
public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
```

### Firebase Realtime Database Configuration
- Setup rules for read/write permissions
- Configure indexing for fast search
- Regular data backup

## 🤝 Contributing

1. Fork this repository
2. Create new branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Create Pull Request

## 📝 License

This project is licensed under the MIT License. All rights reserved.

**Copyright © 2024 - tqc7701@gmail.com**

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

## 📞 Contact

- **Developer**: TU QUANG CHUONG
- **Email**: tqc7701@gmail.com
- **Project**: TimTro - Room Rental Finder App
- **GitHub**: [Project Repository](https://github.com/your-username/ProjectLTMB4)

## 🙏 Acknowledgments

- Firebase Documentation
- Google Maps Platform
- PayPal Developer Documentation
- Material Design Guidelines

---

**⭐ If this project is helpful, please give us a star!**
