package com.gesecur.app.ui.profile

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentUserProfileBinding
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.incidences.IncidencesFragment
import com.gesecur.app.ui.incidences.IncidencesViewModel
import com.gesecur.app.ui.operator.OperatorActivity
import com.gesecur.app.ui.operator.OperatorFragmentDirections
import com.gesecur.app.utils.showConfirm
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@ToolbarOptions(
    showToolbar = true,
    titleRes = R.string.USER_PROFILE_TITLE,
homeButton = GesecurToolbar.HomeButton.BACK,
)
class UserProfileFragment : BaseFragment(R.layout.fragment_user_profile) {

    private val binding by viewBinding(FragmentUserProfileBinding::bind)

    private val viewModel by viewModel<PersonalViewModel>()
    private val incidenceViewModel by sharedViewModel<IncidencesViewModel>()

    override fun setupViews() {

        incidenceViewModel.mainViewAction.observe(viewLifecycleOwner) {
            when (it) {
                is IncidencesViewModel.MainAction.navigateToAddIncidence -> {
                    navigateToIncidences()
                }
            }
        }

        with(binding) {
            optionExpenses.setOnClickListener { navigateToExpenses() }
            optionMileage.setOnClickListener { navigateToMileage() }
            optionIncidences.setOnClickListener { navigateToIncidences() }
            optionCloseSession.setOnClickListener { closeSession() }
        }

        (requireActivity() as OperatorActivity).hideToolbarExtra();
    }

    override fun setupViewModels() {
        /**incidenceViewModel.mainViewAction.observe(viewLifecycleOwner) {
            Log.e("Prueba", "IncidencesList")
            when (it) {
                is IncidencesViewModel.MainAction.navigateToAddIncidence -> {
                    navigateToIncidences()
                }
            }
        }*/
    }

    private fun navigateToExpenses() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToPersonalExpensesListFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToMileage() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToPersonalMileageListFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToIncidences() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToPersonalIncidenceListFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun closeSession() {
        showConfirm(R.string.PROFILE_CLOSE_SESSION_QUESTION, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1 == DialogInterface.BUTTON_POSITIVE) {
                    viewModel.closeSession()
                    navigateToSplash()
                }
            }
        })
    }

    /**private fun navigateOnIncidencesSelected() {
        with(childFragmentManager.findFragmentById(R.id.nav_operator_tab) as NavHostFragment) {
            val navController = navController

            navController.setGraph(R.navigation.nav_operator)
            navController.navigate(R.id.nav_operator_incidences)
        }
    }*/
}