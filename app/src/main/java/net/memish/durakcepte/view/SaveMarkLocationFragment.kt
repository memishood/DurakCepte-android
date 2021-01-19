package net.memish.durakcepte.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.gms.location.*
import es.dmoral.toasty.Toasty
import net.memish.durakcepte.R
import net.memish.durakcepte.databinding.FragmentSaveMarkLocationBinding
import net.memish.durakcepte.model.LatLng
import net.memish.durakcepte.model.MarkLocation
import net.memish.durakcepte.util.Utils
import net.memish.durakcepte.viewmodel.SaveMarkLocationViewModel
import java.io.File
import java.util.Date

class SaveMarkLocationFragment : Fragment() {
    private var binding: FragmentSaveMarkLocationBinding? = null
    private lateinit var viewModel: SaveMarkLocationViewModel

    private var path: String? = null
    private var latLng: LatLng? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveMarkLocationBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.saveMarkLocationAddImage?.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 764)
        }
        binding?.saveMarkLocationAddButton?.setOnClickListener {
            val title = binding?.saveMarkLocationTitle?.text?.toString()
            val description = binding?.saveMarkLocationDescription?.text?.toString()

            if (title == null || title.trim().isEmpty()) {
                Toasty.info(it.context, getString(R.string.fragment_save_mark_location_title_error), Toasty.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (description == null || description.trim().isEmpty()) {
                Toasty.info(it.context, getString(R.string.fragment_save_mark_location_description_error), Toasty.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (latLng == null) {
                Toasty.error(it.context, getString(R.string.fragment_save_mark_location_location_error), Toasty.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            insert(title, description, latLng)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[SaveMarkLocationViewModel::class.java]
        viewModel.locationMutableLiveData.observe(viewLifecycleOwner) {
            latLng = it
            binding?.saveMarkLocationAddress?.text = address
            binding?.saveMarkLocationProgressBar?.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        path = Utils.getFilePath(context, data?.data ?: return)
        binding?.saveMarkLocationAddImage?.scaleType = ImageView.ScaleType.CENTER_CROP
        binding?.saveMarkLocationAddImage?.load(File(path ?: return))
    }

    private fun insert(title: String?, description: String?, latLng: LatLng?) {
        val markLocation = MarkLocation (title, description, path, address, latLng?.latitude, latLng?.longitude, Date().time)
        viewModel.insert(markLocation)
        findNavController().navigateUp()
    }

    private val _address: String?
        get() {
            latLng?.let {
                return Geocoder(context)
                    .getFromLocation(it.latitude, it.longitude, 1)
                    ?.firstOrNull()
                    ?.getAddressLine(0)
            } ?: return null
        }

    private val address: String?
        get() {
            return try {
                _address
            }catch (e: Exception) {
                null
            }
        }

    override fun onResume() {
        super.onResume()
        when (PermissionsFragment.hasContext(context)) {
            true -> viewModel.findPlace()
            false -> findNavController().navigate(R.id.action_saveMarkLocationFragment_to_permissionsFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.destroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}