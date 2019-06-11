package com.urfa.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.urfa.R
import com.urfa.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_imageview.*

class ImageViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val path = intent.getStringExtra("path")
        Glide.with(this).load(path).into(imageView)
    }

    companion object {
        fun newInstance(context: Context, path: String): Intent {
            return Intent(context, ImageViewActivity::class.java)
                .putExtra("path", path)
        }
    }
}