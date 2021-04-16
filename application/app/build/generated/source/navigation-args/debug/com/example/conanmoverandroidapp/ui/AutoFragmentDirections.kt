package com.example.conanmoverandroidapp.ui

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.conanmoverandroidapp.R

class AutoFragmentDirections private constructor() {
    companion object {
        fun actionAutoFragmentToManualFragment(): NavDirections =
                ActionOnlyNavDirections(R.id.action_autoFragment_to_manualFragment)

        fun actionAutoFragmentToPathFragment(): NavDirections =
                ActionOnlyNavDirections(R.id.action_autoFragment_to_pathFragment)
    }
}
