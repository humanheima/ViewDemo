package com.xx.reader.utils

import android.util.Log
import com.google.gson.Gson
import java.lang.reflect.Type


/**
 * Created by p_dmweidu on 2022/6/14
 * Desc: Json 工具类
 */
class JsonUtilKt private constructor() {


    companion object {

        private const val TAG: String = "JsonUtilKt"
        private val gson: Gson = Gson()

        @JvmStatic
        fun toJson(obj: Any?): String {
            return gson.toJson(obj)
        }

        @JvmStatic
        fun <T> toObject(json: String?, classOfT: Class<T>): T? {
            return try {
                gson.fromJson(json, classOfT)
            } catch (e: Exception) {
                Log.d(TAG, "toObject: error ${e.message}")
                null
            }
        }

        @JvmStatic
        fun <T> toList(json: String?, type: Type): List<T>? {
            return try {
                gson.fromJson(json, type)
            } catch (e: Exception) {
                Log.d(TAG, "toObject: error ${e.message}")
                null
            }
        }
    }


}
