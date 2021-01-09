package com.example.appgithub.model

/**
 * Created by arieloliveira on 08/01/21 for AppGitHub.
 */

class Item (
    val name: String,
    val full_name: String,
    val owner: Owner,
    val stargazers_count: Int,
    val forks: Int
)