package com.padc.grocery.data.models

import android.graphics.Bitmap
import com.padc.grocery.data.vos.GroceryVO
import com.padc.grocery.network.FirebaseApi
import com.padc.grocery.network.remoteconfig.FirebaseRemoteConfigManager
import com.padc.grocery.network.auth.AuthManager

interface GroceryModel {

    var mFirebaseApi : FirebaseApi
    var mFirebaseAuthManager : AuthManager
    var mFirebaseRemoteConfigManager : FirebaseRemoteConfigManager

    // storage
    fun getGroceries(onSuccess: (List<GroceryVO>) -> Unit, onFaiure: (String) -> Unit)

    fun addGrocery(name: String ,description : String, amount: Int, image: String)

    fun removeGrocery(name: String)

    fun uploadImageAndUpdateGrocery(grocery : GroceryVO, image : Bitmap?)

    // auth
    fun getUserName(): String

    // remote config
    fun setUpRemoteConfigWithDefaultValues()

    fun fetchRemoteConfigs()

    fun getAppNameFromRemoteConfig() : String

    fun getViewType() : Int
}