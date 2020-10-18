package com.padc.grocery.mvp.presenters.impls

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.padc.grocery.data.models.GroceryModelImpl
import com.padc.grocery.data.models.GroceryModelImpl.mFirebaseAuthManager
import com.padc.grocery.data.vos.GroceryVO
import com.padc.grocery.mvp.presenters.AbstractBasePresenter
import com.padc.grocery.mvp.presenters.MainPresenter
import com.padc.grocery.mvp.views.MainView
import com.padc.grocery.network.auth.FirebaseAuthManager

class MainPresenterImpl : MainPresenter, AbstractBasePresenter<MainView>() {

    private val mGroceryModel = GroceryModelImpl
    private var mChosenGroceryForFileUpload: GroceryVO? = null

    override fun onTapAddGrocery(name: String, description: String, amount: Int) {
        mGroceryModel.addGrocery(name, description, amount, "")
    }

    override fun onPhotoTaken(bitmap: Bitmap) {
        mChosenGroceryForFileUpload?.let {
            mGroceryModel.uploadImageAndUpdateGrocery(it, bitmap)
        }
    }

    override fun onUploadPhotoAndGrocery(
        groceryVO: GroceryVO,
        bitmap: Bitmap?
    ) {
        mGroceryModel.uploadImageAndUpdateGrocery(groceryVO, bitmap)
    }

    override fun onTapEditGrocery(name: String, description: String, amount: Int, image: String) {
        mView.showGroceryDialog(name, description, amount.toString(), image)
    }

    override fun onTapFileUpload(grocery: GroceryVO) {
        mChosenGroceryForFileUpload = grocery
        mView.openGallery();
    }

    override fun onUiReady(owner: LifecycleOwner) {
        mGroceryModel.getGroceries(
            onSuccess = {
                mView.showGroceryData(it)
            },
            onFaiure = {
                mView.showErrorMessage(it)
            }
        )

        mView.showUserName(mGroceryModel.getUserName())

        mView.displayToolbarTitle(mGroceryModel.getAppNameFromRemoteConfig())
    }


    override fun onTapDeleteGrocery(name: String) {
        mGroceryModel.removeGrocery(name)
    }
}