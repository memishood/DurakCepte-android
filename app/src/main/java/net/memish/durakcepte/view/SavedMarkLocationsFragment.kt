package net.memish.durakcepte.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import net.memish.durakcepte.R
import net.memish.durakcepte.adapter.SavedMarkLocationsAdapter
import net.memish.durakcepte.databinding.FragmentSavedMarkLocationsBinding
import net.memish.durakcepte.model.MarkLocation
import net.memish.durakcepte.viewmodel.SavedMarkLocationsViewModel

class SavedMarkLocationsFragment : Fragment(), SavedMarkLocationsAdapter.SavedMarkLocationsClickListener {
    private var binding: FragmentSavedMarkLocationsBinding? = null

    private val adapter = SavedMarkLocationsAdapter(this)
        .also { it.setHasStableIds(true) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedMarkLocationsBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.savedMarkLocationsProgressBar?.visibility = View.VISIBLE
        binding?.savedMarkLocationsAddButton?.setOnClickListener {
            it.findNavController().navigate(R.id.action_savedMarkLocationsFragment_to_saveMarkLocationFragment)
        }
        binding?.savedMarkLocationsRecyclerView?.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this)[SavedMarkLocationsViewModel::class.java]
        viewModel.markLocationsMutableLiveData.observe(viewLifecycleOwner) {
            adapter.update(it)
            binding?.savedMarkLocationsLinearLayout?.visibility = when (it.isEmpty()) {
                true -> View.VISIBLE
                else -> View.GONE
            }
            binding?.savedMarkLocationsProgressBar?.visibility = View.GONE
            binding?.savedMarkLocationsRecyclerView?.smoothScrollToPosition(0)
        }
        viewModel.getData()
    }

    override fun onClick(markLocation: MarkLocation) {
        findNavController().navigate(
            R.id.action_savedMarkLocationsFragment_to_savedMarkLocationDetailsFragment,
            bundleOf("markLocation" to markLocation)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}