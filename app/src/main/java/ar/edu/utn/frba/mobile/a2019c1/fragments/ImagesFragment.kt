package ar.edu.utn.frba.mobile.a2019c1.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.R
import ar.edu.utn.frba.mobile.a2019c1.adapters.ImagesAdapter
import kotlinx.android.synthetic.main.fragment_images.view.*

const val SHOW_GRID: String = "SHOW_GRID"

class ImagesFragment : Fragment() {
    private var columnCount = 3
    private var showGrid: Boolean = true
    private lateinit var editedPictures: List<Uri>
    private var listener: ImagesFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            showGrid = it.getBoolean(SHOW_GRID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_images, container, false)

        editedPictures = getEditedPictures()

        if(editedPictures.any()){
           view.images_list_title.text = "Imágenes editadas previamente"
        } else {
            view.images_list_title.text = "Aquí aparecerán las imágenes editadas.\nPara comenzar a editar, presiona el botón en la parte inferior derecha de la pantalla."
        }

        configureRecyclerView(view.list)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ImagesFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ImagesFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun changeViewToList() {
        showGrid = false

        configureRecyclerView(view!!.list)
    }

    fun changeViewToGrid() {
        showGrid = true

        configureRecyclerView(view!!.list)
    }

    private fun getEditedPictures(): List<Uri> {
        return ArrayList()
    }

    private fun configureRecyclerView(list: RecyclerView) {
        with(list) {
            layoutManager = if (showGrid) GridLayoutManager(context, columnCount) else LinearLayoutManager(context)
            adapter = ImagesAdapter(editedPictures, if (showGrid) columnCount else 1, listener)
        }
    }

    interface ImagesFragmentInteractionListener {
        fun onImageClick(imageUri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(showGrid: Boolean) =
            ImagesFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(SHOW_GRID, showGrid)
                }
            }
    }
}
