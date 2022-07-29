package com.payback.android.pixabay.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.payback.android.pixabay.databinding.MainActivityBinding
import com.payback.android.pixabay.util.makeStatusBarTransparent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //makeStatusBarTransparent()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
