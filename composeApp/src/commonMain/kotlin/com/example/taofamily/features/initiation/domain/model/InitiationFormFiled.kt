package com.example.taofamily.features.initiation.domain.model

import kotlinx.serialization.Serializable

data class InitiationFormFiled(
    val id: Long, // Primary key
    val personId: String,
    val personName: String,
    val personAge: Int, // Numeric
    val contact: String,
    val gender: Gender, // Enum
    val education: String,
    val fullAddress: String,
    val masterName: Master, // Enum
    val introducerName: String,
    val guarantorName: String,
    val templeName: Temple, // Enum
    val initiationDate: String, // YYYY-MM-DD format
    val meritFee: Double, // Numeric
    val is2DaysDharmaClassAttend: Boolean,
    val dharmaMeetingDate: String // YYYY-MM-DD format
){
    // Factory method to create an empty, default instance for the form screen
    companion object{
        fun empty(): InitiationFormFiled{
            return InitiationFormFiled(
                id = 0L, // 0L conventionally means a new entry
                personId = "",
                personName = "",
                personAge = 0,
                contact = "",
                gender = Gender.NONE,
                education = "",
                fullAddress = "",
                masterName = Master.NONE,
                introducerName = "",
                guarantorName = "",
                templeName = Temple.NONE,
                initiationDate = "",
                meritFee = 0.0,
                is2DaysDharmaClassAttend = false,
                dharmaMeetingDate = ""
            )
        }

        fun dummyData(): List<InitiationFormFiled> {
            return listOf(
                InitiationFormFiled(
                    id = 1L,
                    personId = "P001",
                    personName = "John Doe",
                    personAge = 35,
                    contact = "123-456-7890",
                    gender = Gender.MALE,
                    education = "Bachelor's Degree",
                    fullAddress = "123 Main St, Anytown, USA",
                    masterName = Master.MASTER_WANG,
                    introducerName = "Jane Smith",
                    guarantorName = "Peter Jones",
                    templeName = Temple.GOLDEN_SHRINE,
                    initiationDate = "2023-01-15",
                    meritFee = 100.0,
                    is2DaysDharmaClassAttend = true,
                    dharmaMeetingDate = "2023-01-10"
                ),
                InitiationFormFiled(
                    id = 2L,
                    personId = "P002",
                    personName = "Alice Williams",
                    personAge = 28,
                    contact = "987-654-3210",
                    gender = Gender.MALE,
                    education = "Master's Degree",
                    fullAddress = "456 Oak Ave, Sometown, USA",
                    masterName = Master.MASTER_LI,
                    introducerName = "John Doe",
                    guarantorName = "Emily Brown",
                    templeName = Temple.UNITY_TEMPLE,
                    initiationDate = "2023-03-20",
                    meritFee = 150.50,
                    is2DaysDharmaClassAttend = false,
                    dharmaMeetingDate = "2023-03-15"
                )
            )
        }
    }


}
