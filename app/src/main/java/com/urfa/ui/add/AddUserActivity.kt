package com.urfa.ui.add

import android.os.Bundle
import com.urfa.R
import com.urfa.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUserActivity : BaseActivity(), AddUserNavigation {
    val viewModel: AddUserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
    }

    private fun setupViewModel(){
        viewModel.setNavigation(this)
    }
}