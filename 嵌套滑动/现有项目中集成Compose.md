# 在View体系中嵌入Compose



项目级别的build.gradle

```gradle
android {

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

}

dependencies {

   // compose 开始
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // compose 结束
}
```

注意：androidx.appcompat:appcompat 的依赖版本也有要求。使用1.10版本的时候，会报错

```xml
java.lang.IllegalStateException: ViewTreeLifecycleOwner not found from android.widget.ScrollView{36cbbcb VFED.V... ......I. 0,0-0,0 #7f0a0336 app:id/scroll_view_root}
                                     	at androidx.compose.ui.platform.WindowRecomposer_androidKt.createLifecycleAwareWindowRecomposer(WindowRecomposer.android.kt:352)
                                     	at androidx.compose.ui.platform.WindowRecomposer_androidKt.createLifecycleAwareWindowRecomposer$default(WindowRecomposer.android.kt:325)
                                     	at androidx.compose.ui.platform.WindowRecomposerFactory$Companion.LifecycleAware$lambda$0(WindowRecomposer.android.kt:168)
                                     	at androidx.compose.ui.platform.WindowRecomposerFactory$Companion.$r8$lambda$FWAPLXs0qWMqekhMr83xkKattCY(Unknown Source:0)
                                     	at androidx.compose.ui.platform.WindowRecomposerFactory$Companion$$ExternalSyntheticLambda0.createRecomposer(D8$$SyntheticClass:0)
                                     	at androidx.compose.ui.platform.WindowRecomposerPolicy.createAndInstallWindowRecomposer$ui_release(WindowRecomposer.android.kt:224)
                                     	at androidx.compose.ui.platform.WindowRecomposer_androidKt.getWindowRecomposer(WindowRecomposer.android.kt:300)
                                     	at androidx.compose.ui.platform.AbstractComposeView.resolveParentCompositionContext(ComposeView.android.kt:244)
                                     	at androidx.compose.ui.platform.AbstractComposeView.ensureCompositionCreated(ComposeView.android.kt:251)
                                     	at androidx.compose.ui.platform.AbstractComposeView.onAttachedToWindow(ComposeView.android.kt:283)

```

升级到1.6.1版本可以正常使用。


