package com.example.appgithub.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appgithub.R
import com.example.appgithub.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_view.view.*

/**
 * Created by arieloliveira on 08/01/21 for AppGitHub.
 */
class RepositoriesAdapter (var movieList: MutableList<Item>) :
    RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList.get(position)
        holder.onBind(movie)

    }

    fun updateList(newList: MutableList<Item>) {
        this.movieList.removeAll(movieList)
        this.movieList = newList
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: Item) {
            itemView.txtNomeRepositorio.text = item.name
            itemView.ForksRepo.text = item.forks.toString()
            itemView.txtNomeUsuario.text = item.owner.login
            Picasso.get().load(item.owner.avatar_url).into(itemView.imgFotoPerfilPull)
        }
    }
}