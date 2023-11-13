package fr.delcey.todok.data.utils

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String): T? = try {
    fromJson<T>(json, object : TypeToken<T>() {}.type)
} catch (e: JsonParseException) {
    null
}