plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
    id("com.diffplug.spotless")
}

android {
    namespace = "com.flexnet"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    dependencies {
        // dagger
        implementation(libs.dagger)
        ksp(libs.dagger.compiler)

        // retrofit
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.okhttp)
        implementation(libs.logging.interceptor)

        // compose
        implementation(libs.androidx.activity.compose)
        implementation(libs.ui)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.navigation.compose)
        implementation(libs.ui.tooling.preview)
        implementation(libs.ui.tooling)

        //  implementation(libs.androidx.material)
        implementation(libs.androidx.foundation)
        implementation(libs.androidx.constraintlayout.compose)
        implementation(libs.androidx.compose.material3.material3)
        implementation(libs.accompanist.systemuicontroller)
        implementation(libs.androidx.foundation.layout.android)

        // room
        ksp(libs.androidx.room.compiler)
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
    }
}

spotless {
    kotlin {
        target("**/*.kt") // Applies to all Kotlin files
        ktlint("0.50.0") // Ensure you use a compatible version of KtLint
        //  licenseHeaderFile(rootProject.file("spotless.license.kt")) // Optional: License header
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    kotlinGradle {
        target("*.gradle.kts") // Applies to Gradle Kotlin DSL files
        ktlint()
    }

    format("xml") {
        target("**/*.xml")
        indentWithSpaces()
        trimTrailingWhitespace()
    }

    format("misc") {
        target("*.md", "*.yaml", "*.yml", ".gitignore")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

// apply(from = rootProject.file("gradle/spotless-configuration.gradle.kts"))
apply(from = rootProject.file("gradle/publish-package.gradle.kts"))
