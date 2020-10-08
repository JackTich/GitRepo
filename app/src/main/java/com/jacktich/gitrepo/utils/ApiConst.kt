package com.jacktich.gitrepo.utils

object ApiConst {

    //All repositories
    const val URL_ALL_REPO = "/repositories"
    const val PARAM_SINCE_ALL_REPO = "since"

    //Authorization in browser
    const val URL_AUTH = "https://github.com/login/oauth/authorize"
    const val PARAM_CLIENT_ID = "?client_id="
    const val PARAM_SCOPE = "&scope=repo"
    const val PARAM_REDIRECT_URI = "&redirect_uri="

    //Repositories for user
    const val URL_USER_REPO = "/user/repos"

    //Get access token
    const val URL_GET_ACCESS_TOKEN = "https://github.com/login/oauth/access_token"

    //Search
    const val URL_SEARCH_ALL_REPO = "/search/repositories"
    const val PARAM_TITLE_REPOS = "q"
}