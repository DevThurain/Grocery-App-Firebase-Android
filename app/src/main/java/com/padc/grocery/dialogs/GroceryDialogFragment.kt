package com.padc.grocery.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.padc.grocery.R
import com.padc.grocery.activities.MainActivity
import com.padc.grocery.mvp.presenters.MainPresenter
import com.padc.grocery.mvp.presenters.impls.MainPresenterImpl
import kotlinx.android.synthetic.main.dialog_add_grocery.*
import kotlinx.android.synthetic.main.dialog_add_grocery.view.*
import java.io.IOException

class GroceryDialogFragment : DialogFragment() {

    companion object {
        const val TAG_ADD_GROCERY_DIALOG = "TAG_ADD_GROCERY_DIALOG"
        const val BUNDLE_NAME = "BUNDLE_NAME"
        const val BUNDLE_DESCRIPTION = "BUNDLE_DESCRIPTION"
        const val BUNDLE_AMOUNT = "BUNDLE_AMOUNT"

        fun newFragment(): GroceryDialogFragment {
            return GroceryDialogFragment()
        }
    }

    private lateinit var mPresenter: MainPresenter
    private var mSelectedBitmap : Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_grocery, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresenter()
        setupListeners(view)

        view.etGroceryName?.setText(arguments?.getString(BUNDLE_NAME))
        view.etDescription?.setText(arguments?.getString(BUNDLE_DESCRIPTION))
        view.etAmount?.setText(arguments?.getString(BUNDLE_AMOUNT))


    }

    private fun setUpPresenter(){
        activity?.let {
            mPresenter = ViewModelProvider(it)[MainPresenterImpl::class.java]
        }
    }

    private fun setupListeners(view: View){
        view.btnAddGrocery.setOnClickListener {
            mSelectedBitmap?.let { bitmap ->
                mPresenter.onUploadPhotoAndGrocery(
                    etGroceryName.text.toString(),
                    etDescription.text.toString(),
                    etAmount.text.toString().toInt(),
                    bitmap
                )
            }
            dismiss()
        }

        view.ivPhoto.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
       startActivityForResult(Intent.createChooser(intent, "Select Picture"), MainActivity.PICK_IMAGE_REQUEST)
    }


    private fun setPhotoInDialog(bitmap: Bitmap){
        view?.ivPhoto?.setImageBitmap(bitmap)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("bitmap","result ok")


            if (data == null || data.data == null) {
                return
            }

            val filePath = data.data
            try {

                filePath?.let {
                    if (Build.VERSION.SDK_INT >= 29) {
                        val source: ImageDecoder.Source =
                            ImageDecoder.createSource( requireActivity().contentResolver, filePath)

                          mSelectedBitmap = ImageDecoder.decodeBitmap(source)
//                        val bitmap = ImageDecoder.decodeBitmap(source)
//                        mPresenter.onPhotoTaken(bitmap)
                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver, filePath
                        )

                        mSelectedBitmap = bitmap
//                        mPresenter.onPhotoTaken(bitmap)
                    }
                    mSelectedBitmap?.let {
                        setPhotoInDialog(it)
                    }
                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}