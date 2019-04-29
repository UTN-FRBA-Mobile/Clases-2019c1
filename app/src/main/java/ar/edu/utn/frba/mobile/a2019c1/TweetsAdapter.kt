package ar.edu.utn.frba.mobile.a2019c1

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_footer.view.*
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_profile.view.*

class TweetsAdapter(private val listener: MainFragment.OnFragmentInteractionListener?): RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    var tweets: List<Tweet> = listOf()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.item_post
        else {
            val hasPicture = tweets[position - 1].image != null // hardcodeado, debería salir del item a mostrar
            if (hasPicture) R.layout.item_image
            else R.layout.item_simple
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        when (viewType) {
            R.layout.item_post -> {
                view.postButton.setOnClickListener {
                    listener?.showFragment(StatusUpdateFragment())
                }
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_simple, R.layout.item_image -> {
                val itemIndex = position - 1 // el primer item es el encabezado
                val tweet = tweets[itemIndex]
                // todo hardcodeado, debería salir del item
                holder.itemView.nameText.text = tweet.name
                holder.itemView.certifiedIcon.visibility = if (tweet.certified) View.VISIBLE else View.GONE
                holder.itemView.usernameText.text = tweet.username
                holder.itemView.tweetContent.text = tweet.content
                holder.itemView.commentCount.text = "${tweet.commentCount}"
                holder.itemView.retweetCount.text = "${tweet.retweetCount}"
                holder.itemView.likeCount.text = "${tweet.likeCount}"
                Picasso.get().load(Uri.parse(tweet.profilePic))
                    .into(holder.itemView.profilePic)
                // solo si hay imagen usar holder.itemView.image
                if (tweet.image != null)
                    Picasso.get().load(Uri.parse(tweet.image))
                        .into(holder.itemView.image)
            }
            else -> {}
        }
    }

    override fun getItemCount(): Int = tweets.size + 1 // el primer item es el encabezado

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}
