package com.example.appgithub.model

import java.lang.annotation.IncompleteAnnotationException

/**
 * Created by arieloliveira on 08/01/21 for AppGitHub.
 */

data class GitHubResponse(
    val total_count: Int,
    val incomplete_results:  Boolean,
    val items: ArrayList<Item>
)