package com.example.taofamily.features.initiation.domain.model

enum class Gender(override val label: String): InternalLabelled{
    MALE("Male"),
    FEMALE("Female"),
    NONE("Select Gender") // Placeholder for initial state and validation
}

enum class Master(override val label: String) : InternalLabelled {
    MASTER_LI("Master Li"),
    MASTER_CHEN("Master Chen"),
    MASTER_WANG("Master Wang"),
    NONE("Select Master") // Placeholder for initial state and validation
}

enum class Temple(override val label: String) : InternalLabelled {
    UNITY_TEMPLE("Unity Temple"),
    HARMONY_CENTER("Harmony Center"),
    GOLDEN_SHRINE("Golden Shrine"),
    NONE("Select Temple") // Placeholder for initial state and validation
}