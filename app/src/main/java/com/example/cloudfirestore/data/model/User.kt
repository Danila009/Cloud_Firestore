package com.example.cloudfirestore.data.model

import com.google.firebase.firestore.Exclude

data class User(
    var id:String = "",
    var username:String = "",
    var age:Int? = 0,
    val icon:String? = null,
){
    @Exclude
    fun getMap():Map<String, Any>{
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["username"] = username
        age?.let {
            map["age"] = it
        }
        icon?.let {
            map["icon"] = it
        }
        return map
    }
}
