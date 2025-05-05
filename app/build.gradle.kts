import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "me.xmbest.hyper"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.xmbest.hyper"
        minSdk = 33
        targetSdk = 33
        versionCode = 100_000_004
        versionName = "1.0.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("hyperos") {
            storeFile = file("../sign/featHyper")
            storePassword = "hyperos"
            keyAlias = "hyperos"
            keyPassword = "hyperos"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["hyperos"]
            applicationVariants.all {
                outputs.all {
                    if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                        this.outputFileName =
                            "FeatHyper_v${versionName}_${getCommitID()}_${releaseTime()}.apk"
                    }
                }
            }
            manifestPlaceholders["git_info"] = getBranch() + "_" + getCommitID() + "_" + getUserName()
        }
        debug {
            signingConfig = signingConfigs["hyperos"]
            manifestPlaceholders["git_info"] = getBranch() + "_" + getCommitID() + "_" + getUserName()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// 获取发布时间
fun releaseTime(): String {
    val sdf = SimpleDateFormat("yyMMddHHmmss")
    sdf.timeZone = TimeZone.getTimeZone("GMT+08:00")
    return sdf.format(Date())
}

// 获取用户名
fun getUserName(): String {
    return Runtime.getRuntime().exec("git config user.name").inputStream.bufferedReader().use(
        BufferedReader::readText
    ).trim()
}

// 获取当前分支提交ID
fun getCommitID(): String {
    return Runtime.getRuntime().exec("git rev-parse --short HEAD").inputStream.bufferedReader()
        .use(BufferedReader::readText).trim()
}

// 获取当前分支名
fun getBranch(): String {
    val process = Runtime.getRuntime().exec("git rev-parse --abbrev-ref HEAD")
    val branch = process.inputStream.bufferedReader().use(BufferedReader::readText).trim()
    process.waitFor()
    if (process.exitValue() != 0) {
        println(process.errorStream.bufferedReader().use(BufferedReader::readText))
    }
    return branch
}

dependencies {
    // xposed api
    compileOnly(libs.xposed.api)
    // https://developer.android.google.cn/develop/ui/compose/navigation?hl=zh-cn#kts
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}