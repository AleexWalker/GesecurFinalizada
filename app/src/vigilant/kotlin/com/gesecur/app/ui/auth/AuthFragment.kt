package com.gesecur.app.ui.auth

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.BuildConfig
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentAuthBinding
import com.gesecur.app.domain.models.User
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.vigilant.services.ServicesExperimental
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel by viewModel<AuthViewModel>()

    override fun setupViews() {
        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        if(BuildConfig.DEBUG) {
            binding.editCode.setText("409")
        }
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(this) {
            when (it) {
                is AuthViewModel.Action.OnUserLogged -> onUserLogged(it.user)
                is BaseAction.ShowError -> showError(it.error)
                is BaseAction.ShowErrorRes -> showError(getString(it.error))
                is BaseAction.ShowToastError -> showToastBadCode(it.error)
            }
        }
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    private fun doLogin() {
        with(binding) {
            viewModel.login(
                editCode.text.toString()
            )
        }
    }

    private fun onUserLogged(user: User) {
        val intentServicesExperimental = Intent(activity, ServicesExperimental::class.java)
        intentServicesExperimental.putExtra("vigilantId", user.id.toString())
        intentServicesExperimental.putExtra("vigilantCode", binding.editCode.text.toString())
        startActivity(intentServicesExperimental)
    }
}