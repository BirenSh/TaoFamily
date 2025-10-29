package com.example.taofamily.features.initiation.presentation.filter_screen

import com.example.taofamily.features.initiation.domain.model.InternalLabelled

enum class GenderFilterOptions(override val label: String) : InternalLabelled {
    MALE("Male"),
    FEMALE("Female"),
    ALL("Both Genders"),     // Maps to 'true' in ViewModel filter
}

enum class AttendanceFilterOptions(override val label: String) : InternalLabelled {
    ATTENDED("Attended"),   // Maps to entry.is2DaysDharmaClassAttend == true
    NOT_ATTENDED("Not Attended"), // Maps to entry.is2DaysDharmaClassAttend == false
    ALL("Both")   // Maps to 'true' in ViewModel filter
}


enum class TempleFilterOptions(override val label: String) : InternalLabelled {
    UNITY_TEMPLE("Unity Temple"),
    HARMONY_CENTER("Harmony Center"),
    GOLDEN_SHRINE("Golden Shrine"),
    ALl("All Temple")
}