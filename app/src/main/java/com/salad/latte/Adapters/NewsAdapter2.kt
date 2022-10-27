package com.salad.latte.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.ArticleActivity
import com.salad.latte.ClientManagement.ActivityClient
import com.salad.latte.MainActivity
import com.salad.latte.NewsFragment
import com.salad.latte.Objects.News
import com.salad.latte.R
import com.squareup.picasso.Picasso
import java.lang.RuntimeException

class NewsAdapter2(itemsIn :ArrayList<News>, ctxIn :Context) : RecyclerView.Adapter<NewsAdapter2.NewsViewHolder>() {

    var items = itemsIn
    var ctx = ctxIn
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.custom_news_item,parent,false)

        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.title.setText(items.get(position).title)
        holder.description.setText(items.get(position).description)
        try {
            Picasso.get().load(items.get(position).img_url).into(holder.img_url)
        }
        catch (exception :Exception){
            Log.e("NewsAdapter2","Canvas too large to draw "+exception.stackTrace.toString())
        }
        holder.publisher.setText(items.get(position).publisher)
        holder.publish_date.setText(items.get(position).published_utc)
        holder.itemView.setOnClickListener(View.OnClickListener {
            Log.d("NewsAdapter2","Clicked item: "+holder.title.text.toString())
            var articleIntent = Intent(ctx,ArticleActivity::class.java)
            articleIntent.putExtra("article",items.get(position).article_url)
            (ctx as ActivityClient).startActivity(articleIntent)

        })


    }

    override fun getItemCount(): Int {
        return items.size;
    }

    class NewsViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){
        var title = itemView.findViewById<TextView>(R.id.news_title_date)
        var description = itemView.findViewById<TextView>(R.id.news_description)
        var img_url = itemView.findViewById<ImageView>(R.id.news_iv)
        var publisher = itemView.findViewById<TextView>(R.id.news_ticker_tv)
        var publish_date = itemView.findViewById<TextView>(R.id.news_date)
    }
}