package com.acuf5928.marvelcharacters.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.acuf5928.marvelcharacters.databinding.FragmentDetailsBinding
import com.acuf5928.marvelcharacters.databinding.FragmentHomeBinding
import com.acuf5928.marvelcharacters.databinding.FragmentSettingsBinding
import com.acuf5928.marvelcharacters.ui.activity.ActivityMain
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class FragmentSettings: Fragment() {
    private var baseContainerBinding: FragmentSettingsBinding? = null
    private val binding get() = baseContainerBinding!!

    private val sharedPref: SharedPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        baseContainerBinding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })

        setupOnClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseContainerBinding = null
    }

    private fun setupOnClickListener() {
        binding.deleteAll.setOnClickListener {
            sharedPref.edit().clear().commit()

            val i = Intent(context, ActivityMain::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            activity?.let {
                it.startActivity(i)
                it.finish()
            }
        }
    }
}