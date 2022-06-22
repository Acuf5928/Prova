package com.acuf5928.marvelcharacters.ui.activity


import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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

            animate(binding?.homeBtn)

            findNavController(R.id.navigationView).navigate(R.id.action_fragmentHome)
        }
        binding?.searchBtn?.setOnClickListener {
            binding?.homeBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.searchBtn?.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.settingsBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)

            animate(binding?.searchBtn)

            findNavController(R.id.navigationView).navigate(R.id.action_fragmentFavorite)
        }
        binding?.settingsBtn?.setOnClickListener {
            binding?.homeBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.searchBtn?.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            binding?.settingsBtn?.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)

            animate(binding?.settingsBtn)

            findNavController(R.id.navigationView).navigate(R.id.action_fragmentSettings)
        }
    }

    private fun animate(endView: View?) {
        val start = binding?.indicator?.x ?: 0F
        val end = (endView?.x ?: 0F)
            .plus((endView?.width ?: 0).div(2))
            .minus((binding?.indicator?.width ?: 0).div(2))

        ValueAnimator.ofFloat(start, end).apply {
            duration = 330L
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { animator ->
                binding?.indicator?.x = animator.animatedValue as Float
                binding?.indicator?.requestLayout()
            }
            start()
        }
    }
}