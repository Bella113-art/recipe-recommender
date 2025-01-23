import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.reciperecommender"
    compileSdk = 35 // compileSdk 설정

    // ✅ local.properties에서 API 키 가져오기
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    val spoonacularApiKey = properties.getProperty("SPOONACULAR_API_KEY") ?: ""

    defaultConfig {
        applicationId = "com.example.reciperecommender"
        minSdk = 21
        targetSdk = 35 // targetSdk 설정
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ✅ BuildConfig에 API 키 추가 (local.properties에서 가져온 값 사용)
        buildConfigField("String", "SPOONACULAR_API_KEY", "\"$spoonacularApiKey\"")
    }

    // ✅ BuildConfig 활성화 추가 (buildConfigField의 경고 해결!)
    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose Libraries
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation("androidx.compose.material3:material3:1.2.0") // 최신 안정화된 Material3 버전 추가

    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // API를 가져오는 통신 라이브러리
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit (API 통신)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON 데이터를 객체로 변환

    implementation("androidx.navigation:navigation-compose:2.7.2") // 📌 Navigation 라이브러리 추가

    // Debugging
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("io.coil-kt:coil-compose:2.2.2") // ✅ Coil 라이브러리 추가

}
