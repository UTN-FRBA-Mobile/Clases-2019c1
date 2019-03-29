package ar.edu.utn.frba.mobile.a2019c1.Dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.Movie
import ar.edu.utn.frba.mobile.a2019c1.R

class MyAdapter(private val myDataset: MutableList<Movie>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val VIEWTYPE_CATEGORY: Int = 1
    private val VIEWTYPE_MOVIE: Int = 2

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        val view : View = if(viewType == VIEWTYPE_CATEGORY)
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_listitem_category, parent, false)
        else
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_listitem_movie, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.movie_name).text = myDataset[position].movieName
        if(myDataset[position].moviePoster != null)
            holder.view.findViewById<ImageView>(R.id.movie_poster).setImageResource(myDataset[position].moviePoster!!)
    }

    override fun getItemViewType(position: Int): Int {
        return if(myDataset[position].IsCategory)
            VIEWTYPE_CATEGORY
        else
            VIEWTYPE_MOVIE
    }

    override fun getItemCount() = myDataset.size
}