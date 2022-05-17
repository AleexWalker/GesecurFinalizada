package com.gesecur.app.ui.vigilant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.ActivityVigilantBinding
import com.gesecur.app.ui.common.base.BaseActivity
import com.gesecur.app.ui.vigilant.services.ServicesExperimental

class VigilantActivity : BaseActivity(R.layout.activity_vigilant) {

    companion object {
        fun createIntent(context: Context) = Intent(context, VigilantActivity::class.java)
    }


    private val binding by viewBinding(ActivityVigilantBinding::bind)

    override fun setupViews() {
        toolbar = binding.toolbar
    }

    override fun setupViewModels() {
        //Nothing
    }

    fun setToolbarExtra(image: Int, callback: () -> Unit) {
        binding.toolbar.setExtra(image, callback)
    }

    fun hideToolbarExtra() {
        binding.toolbar.hideExtra()
    }

}