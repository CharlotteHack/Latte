package com.salad.latte

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.salad.latte.Adapters.NewsAdapter2
import com.salad.latte.Objects.News
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.json.JSONObject
import java.util.*


class NewsFragment : Fragment() {
    lateinit var news_rv :RecyclerView
    lateinit var news_et :EditText
    var newsItems = ArrayList<News>()
    lateinit var newsAdapter :NewsAdapter2
    lateinit var newsProgress : ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_news, container, false);
        news_rv = v.findViewById(R.id.news_rv)
        news_et = v.findViewById(R.id.news_et)
        newsProgress = v.findViewById(R.id.news_spinner)
        newsProgress.visibility = View.VISIBLE
        news_rv.visibility = View.GONE
        news_rv.layoutManager =  LinearLayoutManager(activity)
        newsAdapter = NewsAdapter2(newsItems,context!!)
        news_rv.adapter = newsAdapter
        fetchNews()
        news_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                fetchNews()
            }
        })


        return v;
    }
    private fun fetchNews(){
        var newsObserable = Observable.create<String>{ emitter ->
            //Do work, get the raw text
            //Use polygon API here
            //https://api.polygon.io/v2/reference/news?apiKey=vGM47_0HjvEz4RB_05YtzyLdSQhddiKgZHCl0d

            //Use polygon API here
            //https://api.polygon.io/v2/reference/news?apiKey=vGM47_0HjvEz4RB_05YtzyLdSQhddiKgZHCl0d
            var url = ""
            // Instantiate the RequestQueue.
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(context)
            url = if (news_et.text.toString().equals("")) {
                "https://api.polygon.io/v2/reference/news?apiKey=vGM47_0HjvEz4RB_05YtzyLdSQhddiKgZHCl0d"
            } else {
                "https://api.polygon.io/v2/reference/news?ticker=" + news_et.text.toString().toUpperCase() + "&apiKey=vGM47_0HjvEz4RB_05YtzyLdSQhddiKgZHCl0d"
            }

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    { response ->
                        emitter.onNext(response)

                    },
                    { emitter.onError(Error()) }
            )

            // Add the request to the RequestQueue.
            queue.add(stringRequest)

        }

        var newsObserver = newsObserable.subscribeBy(
                onNext = {
                    //What to do after work
                    //Convert the text to JSON, create JSON objects, add to list, add list to adapter, update adapter
                    newsProgress.visibility = View.GONE
                    news_rv.visibility = View.VISIBLE
                    convertNewsTextToJSon(it,"")

                },
                onError = {
                    Log.d("NewsFragment",it.stackTrace.toString())
                },
                onComplete = {

                }


        )



    }


    public fun convertNewsTextToJSon(text: String, ticker :String){
        var rootObject = JSONObject(text)
        newsItems.clear()
        for (i in 0..rootObject.getJSONArray("results").length()-1) {
            var news = News()
            var item = rootObject.getJSONArray("results")[i];
            news.title = ( item as JSONObject ).get("title").toString()
            news.description = ( item as JSONObject ).get("description").toString()
            if(news.description.length > 120){
                news.description = news.description.subSequence(0,120).toString()+"..."
            }
            news.img_url =  ( item as JSONObject ).get("image_url").toString()

            news.publisher = ((item as JSONObject).get("publisher") as JSONObject).get("name").toString()
            news.published_utc = ( item as JSONObject ).get("published_utc").toString().split("T")[0]
            news.article_url = ( item as JSONObject ).get("article_url").toString()

            Log.d("NewsFragment","img_url "+news.img_url)
            newsItems.add(news)
        }

        newsAdapter.notifyDataSetChanged()
    }
}