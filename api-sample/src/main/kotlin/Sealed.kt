package com.epam.drill.ts.kt2dts.sampleapi

import kotlinx.serialization.*

sealed class Seal

@Serializable
@SerialName("s1")
data class Seal1(
    val payload: Int?
)

@Serializable
@SerialName("s2")
data class Seal2(
    val payload: String?
)
