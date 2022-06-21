package com.acuf5928.marvelcharacters.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.acuf5928.marvelcharacters.R
import com.acuf5928.marvelcharacters.databinding.FragmentHomeBinding
import com.acuf5928.marvelcharacters.ui.recycler.ElementHomeAdapter
import com.acuf5928.marvelcharacters.viewmodel.ViewModelHome
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

class FragmentHome : Fragment() {
    private var baseContainerBinding: FragmentHomeBinding? = null
    private val binding get() = baseContainerBinding!!

    private val viewModel: ViewModelHome by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        baseContainerBinding = FragmentHomeBinding.inflate(layoutInflater)
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
        setupObserver()
        setupOnChangeListener()

        viewModel.getCharacters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseContainerBinding = null
    }

    private fun setupRecycler() {
        binding.recycler.adapter = ElementHomeAdapter(listOf()) {
            findNavController().navigate(
                R.id.action_fragmentHome_to_fragmentDetails,
                bundleOf("ELEMENT" to it)
            )
        }
    }

    private fun setupObserver() {
        viewModel.isError.observe(viewLifecycleOwner) {
            if (it != null) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Errore di connessione")
                    .setMessage(it.message)
                    .setPositiveButton("OK") { _, _ ->
                    }
                    .show()

                viewModel.isError.value = null
            }
        }

        viewModel.result.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) {
                binding.recycler.visibility = View.GONE
                binding.searchLayout.visibility = View.GONE
                binding.noElement.visibility = View.VISIBLE
            } else {
                binding.recycler.visibility = View.VISIBLE
                binding.searchLayout.visibility = View.VISIBLE
                binding.noElement.visibility = View.GONE

                (binding.recycler.adapter as? ElementHomeAdapter)?.updateList(it.orEmpty())
            }
        }
    }

    private fun setupOnChangeListener() {
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothings
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothings
            }

            override fun afterTextChanged(s: Editable?) {
                (binding.recycler.adapter as? ElementHomeAdapter)?.setFilter(s?.toString().orEmpty())
            }
        })
    }
}