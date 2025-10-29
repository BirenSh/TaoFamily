package com.example.taofamily.core.utils

object AppConstant {
   const val SUCCESS_RESULT = "Success"
   const val FAILED_RESULT = "Failed"

   const val LOADING_FORM = "Loading"

   fun getRandomUniqueId(length: Int = 6): String {
      val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
      return (1..length)
         .map { allowedChars.random() }
         .joinToString("")
   }
}