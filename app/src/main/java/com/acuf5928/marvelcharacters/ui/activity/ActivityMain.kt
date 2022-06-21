package com.acuf5928.marvelcharacters.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.acuf5928.marvelcharacters.R
import com.acuf5928.marvelcharacters.databinding.ActivityMainBinding

class ActivityMain : FragmentActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setupOnClickListener()
    }

    private fun setupOnClickListener() {
        binding?.homeBtn?.setOnClickListener {
            binding?.homeBtn?.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.searchBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.settingsBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)

            findNavController(R.id.navigationView).navigate(R.id.action_fragmentHome)
        }
        binding?.searchBtn?.setOnClickListener {
            binding?.homeBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.searchBtn?.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.settingsBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)

            findNavController(R.id.navigationView).navigate(R.id.action_fragmentFavorite)
        }
        binding?.settingsBtn?.setOnClickListener {
            binding?.homeBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.searchBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.settingsBtn?.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)

            findNavController(R.id.navigationView).navigate(R.id.action_fragmentSettings)
        }
    }
}