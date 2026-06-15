package com.gmail.uli153.ulisestv.ui.navigation

object Destinations {
    const val LOGIN = "login"
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val DETAIL = "detail"
    const val VIDEOS = "videos"
    const val SEARCH = "search"
    const val PLAYER = "player"

    const val ARG_CATEGORY_ID = "categoryId"
    const val ARG_VIDEO_ID = "videoId"
    const val ARG_VIDEO_URL = "videoUrl"

    fun detail(videoId: Int): String = "$DETAIL/$videoId"
    fun videos(categoryId: Int): String = "$VIDEOS?$ARG_CATEGORY_ID=$categoryId"
    fun search(categoryId: Int): String = "$SEARCH?$ARG_CATEGORY_ID=$categoryId"
    fun player(videoUrl: String): String = "$PLAYER?$ARG_VIDEO_URL=$videoUrl"
}
