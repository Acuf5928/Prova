package com.acuf5928.marvelcharacters.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.acuf5928.marvelcharacters.R
import com.acuf5928.marvelcharacters.databinding.FragmentDetailsBinding
import com.acuf5928.marvelcharacters.model.local.MainElementModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject

class FragmentDetails : Fragment() {
    private var baseContainerBinding: FragmentDetailsBinding? = null
    private val binding get() = baseContainerBinding!!

    private val gson: Gson by inject()
    private val sharedPref: SharedPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        baseContainerBinding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupOnClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseContainerBinding = null
    }

    private fun setupView() {
        arguments?.let { arg ->
            (arg.getSerializable("ELEMENT") as? MainElementModel)?.let { model ->
                context?.let {
                    Glide
                        .with(it)
                        .load(model.imgLink)
                        .transform(CircleCrop())
                        .into(binding.icon)
                }

                binding.title.text = model.title
                binding.content.text = model.description

                val list = sharedPref.getString("STAR", "[]")
                val listToken = object : TypeToken<MutableList<MainElementModel>>() {}.type
                val newList: MutableList<MainElementModel> = gson.fromJson(list, listToken)

                if (newList.contains(model)) {
                    binding.star.setImageResource(R.drawable.ic_star_full)
                } else {
                    binding.star.setImageResource(R.drawable.ic_star)
                }
            }
        }
    }

    private fun setupOnClickListener() {
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.share.setOnClickListener {
            arguments?.let { arg ->
                (arg.getSerializable("ELEMENT") as? MainElementModel)?.let { model ->
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, model.title + "\n\n" + model.description)
                    context?.startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
                }
            }
        }
        binding.star.setOnClickListener {
            arguments?.let { arg ->
                (arg.getSerializable("ELEMENT") as? MainElementModel)?.let { model ->
                    val list = sharedPref.getString("STAR", "[]")
                    val listToken = object : TypeToken<MutableList<MainElementModel>>() {}.type
                    val newList: MutableList<MainElementModel> = gson.fromJson(list, listToken)

                    if (newList.contains(model)) {
                        newList.remove(model)
                        binding.star.setImageResource(R.drawable.ic_star)
                    } else {
                        newList.add(model)
                        binding.star.setImageResource(R.drawable.ic_star_full)
                    }

                    sharedPref.edit().putString("STAR", gson.toJson(newList)).apply()
                }
            }
        }
    }
}