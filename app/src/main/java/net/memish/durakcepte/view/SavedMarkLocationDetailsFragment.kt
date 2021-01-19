package net.memish.durakcepte.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import coil.load
import es.dmoral.toasty.Toasty
import net.memish.durakcepte.R
import net.memish.durakcepte.databinding.FragmentSavedMarkLocationDetailsBinding
import net.memish.durakcepte.model.MarkLocation
import net.memish.durakcepte.viewmodel.SavedMarkLocationDetailsViewModel
import java.io.File

class SavedMarkLocationDetailsFragment : Fragment() {

    private var binding: FragmentSavedMarkLocationDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedMarkLocationDetailsBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val markLocation = it.getSerializable("markLocation") as MarkLocation

            markLocation.title?.let {
                binding?.savedMarkLocationDetailsTitle?.text = it
            }

            markLocation.description?.let {
                binding?.savedMarkLocationDetailsDescription?.text = it
            }

            markLocation.address?.let {
                binding?.savedMarkLocationDetailsAddress?.text = it
            }

            markLocation.path?.let { image ->
                binding?.savedMarkLocationDetailsImageView?.visibility = View.VISIBLE
                binding?.savedMarkLocationDetailsImageView?.load(File(image))
            }

            markLocation.createdAt?.let {
                binding?.savedMarkLocationCreatedAt?.text = DateUtils.getRelativeTimeSpanString(it)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this)[SavedMarkLocationDetailsViewModel::class.java]

        viewModel.deletedMarkLocationMutableLiveData.observe(viewLifecycleOwner) {
            Toasty.success(requireContext(), "${it.title} ${getString(R.string.fragment_save_mark_location_details_deleted)}", Toasty.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        arguments?.let { bundle ->
            val markLocation = bundle.getSerializable("markLocation") as MarkLocation

            binding?.savedMarkLocationDetailsDirectionButton?.setOnClickListener {
                val uri = Uri.parse("google.navigation:q=${markLocation.latitude},${markLocation.longitude}")
                Intent(Intent.ACTION_VIEW, uri).apply {
                    `package` = "com.google.android.apps.maps"
                }.also { intent ->
                    startActivity(intent)
                }
            }

            binding?.savedMarkLocationDetailsDeleteButton?.setOnClickListener {
                viewModel.delete(markLocation)
            }

            binding?.savedMarkLocationDetailsShareButton?.setOnClickListener {
                val uri = "https://www.google.com/maps/?q=${markLocation.latitude},${markLocation.longitude}"
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, uri)
                    type = "text/plain"
                }.also { sendIntent ->
                    val shareIntent = Intent.createChooser(sendIntent, markLocation.title)
                    startActivity(shareIntent)
                }
            }

        }
    }
}