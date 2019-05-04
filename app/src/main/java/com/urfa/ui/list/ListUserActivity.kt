package com.urfa.ui.list

import android.os.Bundle
import com.urfa.R
import com.urfa.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListUserActivity : BaseActivity(), ListNavigation {
    val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel(){
        viewModel.setNavigation(this)
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = Adapter(arrayListOf())
    }
}