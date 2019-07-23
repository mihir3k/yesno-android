package com.mihir3k.yesno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val yesNoApiService by lazy {
        YesNoApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showAnswer()

        constraint_layout.setOnTouchListener(object : View.OnTouchListener {
            private val gestureDetector =
                GestureDetector(applicationContext, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        showAnswer()
                        return super.onDoubleTap(e)
                    }
                })

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(event)
                v.performClick()
                return true
            }
        })
    }

    private fun showAnswer() {
        api_answer.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE

        disposable = yesNoApiService.getAnswer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> updateView(result) },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_LONG).show() }
            )
    }

    private fun updateView(result: Model.Result) {
        api_answer.text = result.answer.toUpperCase()

        Glide.with(this)
            .asGif()
            .load(result.image)
            .centerCrop()
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    api_answer.visibility = View.VISIBLE
                    progress_bar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    api_answer.visibility = View.VISIBLE
                    progress_bar.visibility = View.GONE
                    return false
                }
            })
            .into(api_image)
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
