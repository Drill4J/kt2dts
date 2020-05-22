package com.epam.drill.ts.kt2dts.sampleapi

import kotlinx.serialization.*

@Polymorphic
@Serializable
abstract class Poly

@Serializable
@SerialName("ONE")
data class PolyOne(val one: Int) : Poly()

@Serializable
@SerialName("TWO")
data class PolyTwo(val two: String) : Poly()
