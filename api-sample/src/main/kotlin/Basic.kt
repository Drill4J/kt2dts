package com.epam.drill.ts.kt2dts.sampleapi

import kotlinx.serialization.*

@Serializable
data class Data(
    val num: Int,
    val data: Part,
    val optData: String = "",
    val part: Part?,
    val map: Map<String, Part>,
    val list: List<String?>,
    val mapOfLists: Map<Int, List<Part>>
)

@Serializable
data class Part(
    val data: String
)
