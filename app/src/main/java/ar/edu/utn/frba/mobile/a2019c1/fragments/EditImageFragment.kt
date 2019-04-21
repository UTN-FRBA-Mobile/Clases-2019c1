package ar.edu.utn.frba.mobile.a2019c1.fragments

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import ar.edu.utn.frba.mobile.a2019c1.R
import ar.edu.utn.frba.mobile.a2019c1.adapters.ViewPagerAdapter
import ar.edu.utn.frba.mobile.a2019c1.utils.storage.fileSystem.ExternalStorage
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import kotlinx.android.synthetic.main.fragment_edit_image.view.*

const val IMAGE_PATH = "IMAGE_PATH"

class EditImageFragment : Fragment(), FiltersListFragment.FiltersListFragmentListener,
    EditImageDetailsFragment.EditImageDetailsFragmentListener {

    private var listener: OnFragmentInteractionListener? = null
    private var brightnessFinal = 0
    private var saturationFinal = 1.0f
    private var contrastFinal = 1.0f

    private lateinit var imageUri: Uri
    private lateinit var originalImage: Bitmap
    private lateinit var filteredImage: Bitmap
    private lateinit var filtersListFragment: FiltersListFragment
    private lateinit var editImageDetailsFragment: EditImageDetailsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            imageUri = Uri.parse(it.getString(IMAGE_PATH))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_image, container, false)

        originalImage = ExternalStorage.getBitmapFromGallery(activity!!, imageUri, 100, 100)
        filteredImage = originalImage

        view.image_to_edit.setImageBitmap(originalImage)

        setupViewPager(view.viewpager)
        view.tabs.setupWithViewPager(view.viewpager)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onFilterSelected(filter: Filter) {
        resetControls()

        val r = Runnable {
            val mutableCopyOfOriginalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true)

            filteredImage = filter.processFilter(mutableCopyOfOriginalImage)

            activity!!.runOnUiThread { view!!.image_to_edit.setImageBitmap(filteredImage) }
        }

        Thread(r).start()
    }

    override fun onContrastChanged(contrast: Float) {
        contrastFinal = contrast
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrast))
        view!!.image_to_edit.setImageBitmap(myFilter.processFilter(filteredImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onEditStarted() {
    }

    override fun onEditCompleted() {
        val bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        filteredImage = myFilter.processFilter(bitmap)
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        view!!.image_to_edit.setImageBitmap(myFilter.processFilter(filteredImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        view!!.image_to_edit.setImageBitmap(myFilter.processFilter(filteredImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)

        filtersListFragment = FiltersListFragment.newInstance(imageUri)
        filtersListFragment.setListener(this)

        editImageDetailsFragment = EditImageDetailsFragment()
        editImageDetailsFragment.setListener(this)

        adapter.addFragment(filtersListFragment, "Filtros")
        adapter.addFragment(editImageDetailsFragment, "Calibración")

        viewPager.adapter = adapter
    }

    private fun resetControls() {
        if (::editImageDetailsFragment.isInitialized) {
            editImageDetailsFragment.resetControls()
        }
        brightnessFinal = 0
        saturationFinal = 1.0f
        contrastFinal = 1.0f
    }

    fun save() {
    }

    interface OnFragmentInteractionListener

    companion object {
        @JvmStatic
        fun newInstance(imageUri: Uri) =
            EditImageFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_PATH, imageUri.toString())
                }
            }
    }
}
