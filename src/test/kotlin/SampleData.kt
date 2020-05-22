package com.epam.drill.ts.kt2dts

import kotlinx.serialization.*

@Serializable
data class Sample(
    val num: Int = 0,
    val str: String?
)

@Serializable
data class Complex(
    val num: Int,
    val list: List<String>,
    val optList: List<String?>,
    val listOfLists: List<List<Sample>>,
    val listOfOptLists: List<List<Sample>?>,
    val listOfListsOpt: List<List<Sample?>>,
    val mapOfLists: Map<String, List<String>>,
    val mapOfOptLists: Map<String, List<String>?>
)
