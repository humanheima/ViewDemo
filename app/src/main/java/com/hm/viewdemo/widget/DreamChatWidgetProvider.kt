package com.hm.viewdemo.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.RemoteViews
import com.hm.viewdemo.R
import com.hm.viewdemo.activity.MainActivity
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class DreamChatWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateSingleWidget(context, appWidgetManager, appWidgetId, defaultData())
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) {

            // 再模拟网络请求，加载新图片并局部更新
            IMAGE_EXECUTOR.execute {
                // 模拟网络延迟
                Thread.sleep(1000)
                val newData = WidgetData(
                    title = "跟你的梦中人聊聊",
                    brandName = "筑梦岛",
                    characters = listOf(
                        WidgetData.Character("红羽", REFRESH_URL),
                        WidgetData.Character("君山", REFRESH_URL),
                        WidgetData.Character("摄羽", REFRESH_URL),
                        WidgetData.Character("鹤白", REFRESH_URL)
                    )
                )
                refreshWithData(context, newData)
            }
        }
    }

    companion object {
        const val ACTION_REFRESH = "com.hm.viewdemo.action.DREAM_WIDGET_REFRESH"

        private const val DEFAULT_URL =
            "https://imgservices-1252317822.image.myqcloud.com/coco/s11172022/6db4fb37.u34k62.png"

        private const val REFRESH_URL =
            "https://imgservices-1252317822.image.myqcloud.com/coco/s11272024/163fb33f.clpj4j.png"

        private val IMAGE_EXECUTOR = Executors.newFixedThreadPool(2)
        private val IMAGE_CACHE = LruCache<String, Bitmap>(8)

        private val IMAGE_VIEW_IDS = listOf(
            R.id.iv_avatar_1, R.id.iv_avatar_2, R.id.iv_avatar_3, R.id.iv_avatar_4
        )
        private val NAME_VIEW_IDS = listOf(
            R.id.tv_name_1, R.id.tv_name_2, R.id.tv_name_3, R.id.tv_name_4
        )

        fun defaultData() = WidgetData(
            title = "跟你的梦中人聊聊",
            brandName = "筑梦岛",
            characters = listOf(
                WidgetData.Character("红羽", DEFAULT_URL),
                WidgetData.Character("君山", DEFAULT_URL),
                WidgetData.Character("摄羽", DEFAULT_URL),
                WidgetData.Character("鹤白", DEFAULT_URL)
            )
        )

        /**
         * 外部（如 Activity 模拟网络回调后）调用：用新数据刷新所有已添加的小组件
         */
        fun refreshWithData(context: Context, data: WidgetData) {
            val appCtx = context.applicationContext
            val manager = AppWidgetManager.getInstance(appCtx)
            val ids = manager.getAppWidgetIds(ComponentName(appCtx, DreamChatWidgetProvider::class.java))
            if (ids.isEmpty()) return
            ids.forEach { updateSingleWidget(appCtx, manager, it, data) }
        }

        fun updateSingleWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            data: WidgetData
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_dream_chat)
            views.setTextViewText(R.id.tv_title, data.title)
            views.setTextViewText(R.id.tv_brand, data.brandName)

            data.characters.forEachIndexed { i, char ->
                if (i < NAME_VIEW_IDS.size) views.setTextViewText(NAME_VIEW_IDS[i], char.name)
            }

            // 点击打开 App
            val launchIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            views.setOnClickPendingIntent(
                R.id.widget_root,
                PendingIntent.getActivity(
                    context, appWidgetId, launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

            // 刷新按钮
            val refreshIntent = Intent(context, DreamChatWidgetProvider::class.java).apply {
                action = ACTION_REFRESH
            }
            views.setOnClickPendingIntent(
                R.id.iv_refresh,
                PendingIntent.getBroadcast(
                    context, appWidgetId, refreshIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

            appWidgetManager.updateAppWidget(appWidgetId, views)

            // 异步加载图片，局部更新
            data.characters.forEachIndexed { i, char ->
                if (i >= IMAGE_VIEW_IDS.size) return@forEachIndexed
                val viewId = IMAGE_VIEW_IDS[i]
                fetchImageAsync(char.avatarUrl) { bitmap ->
                    bitmap ?: return@fetchImageAsync
                    val partial = RemoteViews(context.packageName, R.layout.widget_dream_chat)
                    partial.setImageViewBitmap(viewId, bitmap)
                    appWidgetManager.partiallyUpdateAppWidget(appWidgetId, partial)
                }
            }
        }

        private fun fetchImageAsync(url: String, callback: (Bitmap?) -> Unit) {
            IMAGE_EXECUTOR.execute {
                val cached = IMAGE_CACHE.get(url)
                if (cached != null) { callback(cached); return@execute }
                val bitmap = downloadBitmap(url)
                if (bitmap != null) IMAGE_CACHE.put(url, bitmap)
                callback(bitmap)
            }
        }

        private fun downloadBitmap(url: String): Bitmap? {
            var connection: HttpURLConnection? = null
            var stream: InputStream? = null
            return try {
                connection = (URL(url).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 10_000
                    readTimeout = 10_000
                    doInput = true
                }
                connection.connect()
                if (connection.responseCode != HttpURLConnection.HTTP_OK) null
                else { stream = connection.inputStream; BitmapFactory.decodeStream(stream) }
            } catch (_: Exception) { null }
            finally { stream?.close(); connection?.disconnect() }
        }
    }
}
