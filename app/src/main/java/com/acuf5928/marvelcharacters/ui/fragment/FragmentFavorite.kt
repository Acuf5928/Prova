package com.acuf5928.marvelcharacters.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.acuf5928.marvelcharacters.R
import com.acuf5928.marvelcharacters.databinding.FragmentFavoriteBinding
import com.acuf5928.marvelcharacters.model.local.MainElementModel
import com.acuf5928.marvelcharacters.ui.recycler.ElementHomeAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject

class FragmentFavorite : Fragment() {
    private var baseContainerBinding: FragmentFavoriteBinding? = null
    private val binding get() = baseContainerBinding!!

    private val gson: Gson by inject()
    private val sharedPref: SharedPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        baseContainerBinding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })

        setupRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseContainerBinding = null
    }

    private fun setupRecycler() {
        val list = sharedPref.getString("STAR", "[]")
        val listToken = object : TypeToken<MutableList<MainElementModel>>() {}.type
        val newList: MutableList<MainElementModel> = gson.fromJson(list, listToken)

        if (newList.isEmpty()) {
            binding.recycler.visibility = GONE
            binding.searchLayout.visibility = GONE
            binding.noElement.visibility = VISIBLE
        } else {
            binding.recycler.visibility = VISIBLE
            binding.searchLayout.visibility = VISIBLE
            binding.noElement.visibility = GONE

            binding.recycler.adapter = ElementHomeAdapter(newList) {
                findNavController().navigate(
                    R.id.action_fragmentFavorite_to_fragmentDetails,
                    bundleOf("ELEMENT" to it)
                )
            }
        }
    }
}