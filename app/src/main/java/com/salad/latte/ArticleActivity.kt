package com.salad.latte

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy

class ArticleActivity : AppCompatActivity(){

    lateinit var article_wv :WebView
    lateinit var article_pb :ProgressBar
    lateinit var articleObserver :Observer<Int>
    lateinit var articleObservable :Observable<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article);
        article_wv = findViewById(R.id.article_wv)
        article_pb = findViewById(R.id.article_progress)
        article_pb.visibility = View.VISIBLE
        article_wv.visibility = View.GONE
        articleObservable = Observable.create { emitter ->
            if (intent.hasExtra("article")) {
                var article_url = intent.getStringExtra("article")
                Log.d("ArticleActivity", "Article URL: " + article_url)

                article_wv.loadUrl(article_url!!)
                emitter.onNext(1)
            } else {
                Toast.makeText(this, "Failed to fetch article", Toast.LENGTH_LONG).show()
            }
        }

        articleObservable.subscribeBy(
        onNext = {
            article_wv.visibility = View.VISIBLE
            article_pb.visibility = View.GONE
            Log.d("ArticleActivity","Article finished loading, removing article PB")
        }
        )




    }
}