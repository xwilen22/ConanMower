package com.example.conanmoverandroidapp.ui

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.conanmoverandroidapp.R

class ManualFragmentDirections private constructor() {
    companion object {
        fun actionManualFragmentToAutoFragment(): NavDirections =
                ActionOnlyNavDirections(R.id.action_manualFragment_to_autoFragment)
    }
}
