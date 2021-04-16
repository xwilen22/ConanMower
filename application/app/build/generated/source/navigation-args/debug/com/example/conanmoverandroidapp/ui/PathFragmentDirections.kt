package com.example.conanmoverandroidapp.ui

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.conanmoverandroidapp.R

class PathFragmentDirections private constructor() {
    companion object {
        fun actionPathFragmentToAutoFragment(): NavDirections =
                ActionOnlyNavDirections(R.id.action_pathFragment_to_autoFragment)
    }
}
