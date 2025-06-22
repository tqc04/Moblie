# 🏠 Ứng Dụng Tìm Nhà Trọ - TimTro App

## 📱 Giới thiệu

**TimTro App** là ứng dụng di động Android giúp người dùng tìm kiếm và đăng tin cho thuê nhà trọ, phòng trọ một cách dễ dàng và tiện lợi. Ứng dụng được phát triển bằng Java với Firebase làm backend và tích hợp nhiều tính năng hiện đại.

## ✨ Tính năng chính

### 🔐 Xác thực và Quản lý tài khoản
- **Đăng ký/Đăng nhập**: Hệ thống xác thực qua Firebase Authentication
- **Quên mật khẩu**: Gửi email đặt lại mật khẩu
- **Quản lý hồ sơ**: Cập nhật thông tin cá nhân
- **Xác thực email**: Bảo mật tài khoản người dùng

### 🏠 Quản lý bài đăng
- **Đăng tin**: Đăng bài cho thuê phòng trọ với đầy đủ thông tin
- **Upload ảnh**: Tải lên hình ảnh phòng trọ (Base64 encoding)
- **Thông tin chi tiết**: Giá cả, diện tích, địa chỉ, mô tả, đối tượng cho thuê
- **Quản lý thời hạn**: Bài đăng có thời hạn hiệu lực
- **Duyệt bài**: Hệ thống phê duyệt bài đăng trước khi hiển thị

### 🔍 Tìm kiếm thông minh
- **Tìm kiếm theo vị trí**: Lọc theo tỉnh/thành phố, quận/huyện
- **Tìm kiếm văn bản**: Tìm kiếm theo từ khóa
- **Bộ lọc nâng cao**: Lọc theo giá, diện tích, loại phòng
- **Hiển thị danh sách**: Giao diện ListView với adapter tùy chỉnh

### 🗺️ Tích hợp bản đồ
- **Google Maps**: Hiển thị vị trí phòng trọ trên bản đồ
- **Định vị GPS**: Lưu và hiển thị tọa độ (latitude, longitude)
- **Chỉ đường**: Tích hợp Google Maps để chỉ đường đến địa điểm
- **Multiple Maps Activities**: Nhiều màn hình bản đồ cho các mục đích khác nhau

### 💳 Hệ thống thanh toán
- **PayPal Integration**: Thanh toán trực tuyến qua PayPal
- **Thanh toán tiền mặt**: Lựa chọn thanh toán trực tiếp
- **Quản lý phí**: Tính phí đăng bài theo loại tin và thời hạn
- **WebView PayPal**: Giao diện thanh toán trong ứng dụng

### 👥 Quản trị hệ thống
- **Admin Panel**: Giao diện quản trị riêng biệt
- **Quản lý người dùng**: Xem, sửa, xóa tài khoản người dùng
- **Quản lý bài viết**: Duyệt, từ chối, xóa bài đăng
- **Thống kê**: Theo dõi hoạt động của ứng dụng

## 🛠️ Công nghệ sử dụng

### Frontend (Android)
- **Ngôn ngữ**: Java
- **SDK**: Android API 24+ (Android 7.0+)
- **UI Framework**: 
  - Material Design Components
  - Navigation Component
  - RecyclerView, CardView
  - Bottom Navigation

### Backend & Database
- **Firebase Realtime Database**: Lưu trữ dữ liệu thời gian thực
- **Firebase Storage**: Lưu trữ hình ảnh
- **Firebase Authentication**: Xác thực người dùng



## 📱 Cấu trúc ứng dụng

### Activities chính
- `MainActivity`: Activity chính với Bottom Navigation
- `LoginActivity`: Màn hình đăng nhập
- `PostActivity`: Đăng bài mới
- `DetailActivity`: Chi tiết bài đăng
- `MapsActivity`: Hiển thị bản đồ
- `AdminActivity`: Panel quản trị

### Fragments
- `HomeFragment`: Trang chủ - danh sách bài đăng
- `SearchFragment`: Tìm kiếm và lọc
- `PostFragment`: Form đăng bài
- `LoginFragment`: Form đăng nhập



## 📥 HƯỚNG DẪN TẢI VÀ CÀI ĐẶT CHI TIẾT

### 🛠️ Chuẩn bị môi trường phát triển

#### 1. Tải và cài đặt Android Studio
```bash
# Bước 1: Tải Android Studio
1. Truy cập: https://developer.android.com/studio
2. Click "Download Android Studio"
3. Chọn phiên bản phù hợp:
   - Windows: android-studio-2023.1.1.28-windows.exe
   - macOS: android-studio-2023.1.1.28-mac.dmg  
   - Linux: android-studio-2023.1.1.28-linux.tar.gz

# Bước 2: Cài đặt
- Windows: Chạy file .exe và làm theo hướng dẫn
- macOS: Kéo Android Studio vào Applications
- Linux: Giải nén và chạy studio.sh
```

#### 2. Cài đặt Java Development Kit (JDK)
```bash
# Kiểm tra Java đã cài chưa
java -version
javac -version

# Nếu chưa có, tải JDK 11:
# Windows: https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe
# macOS: https://download.oracle.com/java/17/latest/jdk-17_macos-x64_bin.dmg
# Linux: sudo apt install openjdk-11-jdk

# Thiết lập JAVA_HOME
export JAVA_HOME=/path/to/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

#### 3. Thiết lập Android SDK
```bash
# Trong Android Studio:
1. Tools → SDK Manager
2. SDK Platforms: Chọn API 24, 30, 35
3. SDK Tools: 
   - Android SDK Build-Tools
   - Android Emulator
   - Android SDK Platform-Tools
   - Google Play services
4. Apply → OK
```

### 📂 Tải source code

#### Cách 1: Clone từ Git (Khuyến nghị)
```bash
# Cài đặt Git nếu chưa có
# Windows: https://git-scm.com/download/win
# macOS: brew install git
# Linux: sudo apt install git

# Clone repository
git clone https://github.com/your-username/ProjectLTMB4.git
cd ProjectLTMB4

# Kiểm tra cấu trúc project
ls -la
# Phải thấy các folder: app/, gradle/, gradlew, settings.gradle
```

#### Cách 2: Tải ZIP file
```bash
1. Truy cập GitHub repository
2. Click nút "Code" → "Download ZIP"
3. Giải nén file ZIP
4. Đổi tên folder thành "ProjectLTMB4"
```

### 🔧 Cấu hình dịch vụ bên ngoài

#### 🔥 Firebase - BƯỚC BẮT BUỘC

**Bước 1: Tạo Firebase Project**
```bash
1. Truy cập: https://console.firebase.google.com
2. Click "Add project"
3. Nhập project name: "TimTro-App"
4. Disable Google Analytics (tùy chọn)
5. Click "Create project"
```

**Bước 2: Thêm Android App**
```bash
1. Click "Add app" → Android icon
2. Điền thông tin:
   - Package name: com.example.finalapp
   - App nickname: TimTro App
   - Debug signing certificate SHA-1: (để trống)
3. Click "Register app"
```

**Bước 3: Tải cấu hình**
```bash
1. Download google-services.json
2. Copy file vào: ProjectLTMB4/app/google-services.json
3. ⚠️ QUAN TRỌNG: Đặt đúng vị trí này
```

**Bước 4: Enable Services**
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

#### 🗺️ Google Maps - BƯỚC BẮT BUỘC

**Bước 1: Tạo Google Cloud Project**
```bash
1. Truy cập: https://console.cloud.google.com
2. New Project → Nhập tên → Create
3. Select project vừa tạo
```

**Bước 2: Enable APIs**
```bash
1. APIs & Services → Library
2. Tìm và enable:
   - Maps SDK for Android
   - Places API
   - Geocoding API
   - Directions API
```

**Bước 3: Tạo API Key**
```bash
1. APIs & Services → Credentials
2. Create Credentials → API Key
3. Copy API key
4. Edit API key:
   - Application restrictions: Android apps
   - Package name: com.example.finalapp
   - SHA-1: (có thể để trống cho development)
```

**Bước 4: Cập nhật code**
```xml
<!-- File: app/src/main/res/values/google_maps_api.xml -->
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">
        PASTE_YOUR_API_KEY_HERE
    </string>
</resources>
```

#### 💳 PayPal - TÙY CHỌN

```bash
1. Truy cập: https://developer.paypal.com
2. Log in → Create App
3. App Type: Merchant
4. Copy Client ID từ Sandbox
5. Cập nhật PayPalConfig.java:
   public static final String PAYPAL_CLIENT_ID = "your_client_id_here";
```

### 🚀 Build và chạy ứng dụng

#### Bước 1: Mở project
```bash
1. Mở Android Studio
2. File → Open
3. Chọn folder ProjectLTMB4
4. Wait for Gradle sync (2-5 phút)
```

#### Bước 2: Kiểm tra dependencies
```bash
# Nếu có lỗi Gradle sync:
1. File → Invalidate Caches → Invalidate and Restart
2. Hoặc: ./gradlew clean build
```

#### Bước 3: Chạy ứng dụng

**Trên thiết bị thật:**
```bash
1. Enable Developer Options:
   - Settings → About Phone → Build Number (tap 7 lần)
2. Enable USB Debugging:
   - Settings → Developer Options → USB Debugging
3. Kết nối USB → Allow Debugging
4. Android Studio: Run → Run 'app'
```

**Trên Emulator:**
```bash
1. Tools → AVD Manager → Create Virtual Device
2. Chọn: Pixel 4 (API 30)
3. Download system image nếu cần
4. Finish → Start emulator
5. Run → Run 'app'
```

### ✅ Kiểm tra cài đặt thành công

```bash
# App chạy thành công khi:
1. ✅ Màn hình splash hiển thị
2. ✅ Bottom navigation có 4 tab
3. ✅ Có thể đăng ký tài khoản mới
4. ✅ Firebase connection hoạt động
5. ✅ Google Maps hiển thị (nếu cấu hình)
6. ✅ Không có crash hoặc force close
```

### 🔧 Xử lý lỗi thường gặp

#### Lỗi "google-services.json missing"
```bash
# Giải pháp:
1. Kiểm tra file google-services.json trong app/
2. Package name phải đúng: com.example.finalapp
3. Restart Android Studio
```

#### Lỗi "API key not found"
```bash
# Giải pháp:
1. Kiểm tra google_maps_api.xml
2. API key phải hợp lệ và enabled
3. Package name restriction phải đúng
```

#### Lỗi "Build failed"
```bash
# Giải pháp:
./gradlew clean
./gradlew build

# Hoặc:
Build → Clean Project
Build → Rebuild Project
```

#### Lỗi "Dependencies not found"
```bash
# Giải pháp:
1. File → Sync Project with Gradle Files
2. Kiểm tra internet connection
3. Check gradle version compatibility
```

---

## 🚀 Cài đặt và chạy

### 📋 Yêu cầu hệ thống
- **Android Studio**: Flamingo (2022.2.1) hoặc mới hơn
- **JDK**: Java 11 hoặc Java 17
- **Android SDK**: API 24+ (Android 7.0+)
- **Target SDK**: API 35 (Android 14)
- **Google Play Services**: Phiên bản mới nhất
- **RAM**: Tối thiểu 8GB (khuyến nghị 16GB)
- **Dung lượng ổ cứng**: Ít nhất 4GB trống

### 📥 Tải xuống và cài đặt

#### Bước 1: Tải Android Studio
1. Truy cập [Android Studio Official](https://developer.android.com/studio)
2. Tải phiên bản phù hợp với hệ điều hành
3. Cài đặt và thiết lập SDK Manager
4. Tải Android SDK API 24-35

#### Bước 2: Tải Java Development Kit (JDK)
```bash
# Kiểm tra Java version
java -version
javac -version

# Nếu chưa có, tải từ:
# Oracle JDK 11: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
# OpenJDK 11: https://openjdk.java.net/projects/jdk/11/
```

#### Bước 3: Clone Repository
```bash
# Clone project từ GitHub
git clone https://github.com/your-username/ProjectLTMB4.git
cd ProjectLTMB4

# Hoặc tải ZIP file và giải nén
# Download ZIP -> Extract -> Mở folder trong Android Studio
```

### ⚙️ Cấu hình chi tiết

#### 🔥 Firebase Setup (BẮT BUỘC)
1. **Tạo Firebase Project**
   - Truy cập [Firebase Console](https://console.firebase.google.com)
   - Click "Add project" → Nhập tên project → Tiếp tục
   - Chọn "Default Account for Firebase" → Create project

2. **Thêm Android App**
   - Click "Add app" → Chọn Android icon
   - **Package name**: `com.example.finalapp` (CHÍNH XÁC)
   - **App nickname**: TimTro App
   - **SHA-1**: (Tùy chọn, dùng cho debug)
   
3. **Tải google-services.json**
   ```bash
   # Tải file google-services.json
   # Đặt vào: ProjectLTMB4/app/google-services.json
   # ✅ QUAN TRỌNG: File này PHẢI đặt đúng vị trí
   ```

4. **Cấu hình Firebase Services**
   - **Authentication**: Email/Password
   - **Realtime Database**: Rules mode "Test" ban đầu
   - **Storage**: Rules mode "Test" ban đầu

#### 🗺️ Google Maps Setup (BẮT BUỘC)
1. **Tạo Google Cloud Project**
   - Truy cập [Google Cloud Console](https://console.cloud.google.com)
   - Tạo project mới hoặc chọn project có sẵn

2. **Enable APIs**
   ```bash
   # Enable các API sau:
   - Maps SDK for Android
   - Places API
   - Geocoding API
   - Directions API
   ```

3. **Tạo API Key**
   - Credentials → Create Credentials → API Key
   - Restrict API key → Android apps
   - Package name: `com.example.finalapp`
   - SHA-1 certificate fingerprint (nếu có)

4. **Cập nhật API Key**
   ```xml
   <!-- File: app/src/main/res/values/google_maps_api.xml -->
   <resources>
       <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">
           YOUR_API_KEY_HERE
       </string>
   </resources>
   ```

#### 💳 PayPal Setup (TÙY CHỌN)
1. **Đăng ký PayPal Developer**
   - Truy cập [PayPal Developer](https://developer.paypal.com)
   - Tạo account → Create App

2. **Lấy Client ID**
   ```java
   // File: app/src/main/java/com/example/finalapp/PayPalConfig.java
   public static final String PAYPAL_CLIENT_ID = "your_sandbox_client_id";
   public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
   ```

### 🔧 Build và chạy

#### Bước 1: Mở project trong Android Studio
```bash
# Mở Android Studio
# File → Open → Chọn folder ProjectLTMB4
# Đợi Gradle sync hoàn thành
```

#### Bước 2: Sync Dependencies
```bash
# Trong Android Studio:
# Tools → Android → Sync Project with Gradle Files
# Hoặc click icon "Sync Now" khi xuất hiện popup
```

#### Bước 3: Build Project
```bash
# Build từ Android Studio:
# Build → Make Project (Ctrl+F9)

# Hoặc từ terminal:
./gradlew assembleDebug

# Cho Windows:
gradlew.bat assembleDebug
```

#### Bước 4: Chạy ứng dụng
```bash
# Kết nối thiết bị Android hoặc tạo AVD
# Run → Run 'app' (Shift+F10)
# Hoặc click nút ▶️ trên toolbar
```

### 📱 Thiết lập thiết bị

#### Thiết bị thật (Khuyến nghị)
1. Bật Developer Options
2. Bật USB Debugging
3. Kết nối USB → Allow debugging
4. Install và test app

#### Android Virtual Device (AVD)
```bash
# Tạo AVD mới:
# Tools → AVD Manager → Create Virtual Device
# Chọn: Pixel 4 hoặc Pixel 6
# API Level: 24-35
# RAM: 2GB+
```

### ⚠️ Troubleshooting thường gặp

#### Lỗi Gradle Sync
```bash
# Giải pháp:
1. File → Invalidate Caches and Restart
2. Xóa folder .gradle trong project
3. Sync lại project
```

#### Lỗi Firebase
```bash
# Kiểm tra:
1. google-services.json đúng vị trí
2. Package name chính xác: com.example.finalapp
3. Google services plugin được apply
```

#### Lỗi Google Maps
```bash
# Kiểm tra:
1. API Key hợp lệ và được restrict đúng
2. Enable đủ các Maps APIs
3. Billing account được thiết lập (nếu cần)
```

#### Lỗi Build
```bash
# Clean và rebuild:
./gradlew clean
./gradlew assembleDebug

# Hoặc trong Android Studio:
# Build → Clean Project
# Build → Rebuild Project
```

### Cấu hình Database Firebase
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

## 📖 Hướng dẫn sử dụng

### Cho người dùng
1. **Đăng ký tài khoản** mới hoặc đăng nhập
2. **Xác thực email** để kích hoạt tài khoản
3. **Tìm kiếm phòng trọ** theo vị trí và tiêu chí
4. **Xem chi tiết** bài đăng và liên hệ chủ trọ
5. **Đăng tin cho thuê** phòng trọ của bạn
6. **Thanh toán phí đăng tin** qua PayPal hoặc tiền mặt

### Cho quản trị viên
1. Đăng nhập với tài khoản admin
2. Quản lý người dùng: xem, chỉnh sửa, xóa
3. Duyệt bài đăng: phê duyệt hoặc từ chối
4. Theo dõi thống kê hệ thống

## 🎨 Giao diện

Ứng dụng sử dụng Material Design với:
- **Bottom Navigation**: Điều hướng chính
- **Card Layout**: Hiển thị danh sách bài đăng
- **Floating Action Button**: Thêm bài đăng mới
- **Search Interface**: Tìm kiếm thông minh
- **Map Integration**: Bản đồ tương tác

## 🔧 Cấu hình nâng cao

### Tùy chỉnh PayPal
```java
// PayPalConfig.java
public static final String PAYPAL_CLIENT_ID = "your_paypal_client_id";
public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
```

### Cấu hình Firebase Realtime Database
- Thiết lập rules cho read/write permissions
- Cấu hình indexing cho tìm kiếm nhanh
- Backup dữ liệu định kỳ

## 🤝 Đóng góp

1. Fork repository này
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Liên hệ

- **Email**: [your-email@example.com]
- **GitHub**: [your-github-profile]

## 🙏 Acknowledgments

- Firebase Documentation
- Google Maps Platform
- PayPal Developer Documentation
- Material Design Guidelines

---

**⭐ Nếu project này hữu ích, hãy cho chúng tôi một star!**
