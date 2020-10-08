package com.jacktich.gitrepo.data.api

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jacktich.gitrepo.data.api.responces.ErrorRepositoriesResponse
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import java.lang.Exception

object RepositoryParser {
    fun parseRepositoryJson(reposJson: JsonArray): Any {
        return try {
            val reposArray = mutableListOf<RepositoriesItem>()
            reposJson.forEach {
                reposArray.add(Gson().fromJson(it, RepositoriesItem::class.java))
            }
            reposArray
        } catch (e: Exception) {
            Gson().fromJson(reposJson, ErrorRepositoriesResponse::class.java)
        }
    }
}