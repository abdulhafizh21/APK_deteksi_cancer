@file:Suppress("DEPRECATION")

package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.HistoryResultRepository
import com.dicoding.asclepius.data.db.HistoryResult
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.ViewModelFactory
import com.dicoding.asclepius.viewmodel.ResultViewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

@Suppress("NAME_SHADOWING")
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var resultViewModel: ResultViewModel
    private lateinit var historyResultRepository: HistoryResultRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyResultRepository = HistoryResultRepository(application)
        resultViewModel = obtainViewModel(this@ResultActivity)

        setTitle()

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }
        startClassify(imageUri)

    }

    private fun startClassify(imageUri: Uri) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { classifications ->
                            if (classifications.isNotEmpty() && classifications[0].categories.isNotEmpty()) {

                                val topCategory = classifications[0].categories.maxByOrNull { it.score }

                                topCategory?.let { category ->
                                    val label = category.label
                                    val score = category.score.toString()

                                    val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI)).toString()
                                    val historyResult = HistoryResult(image = imageUri, label = label, score = score)
                                    resultViewModel.insertBookmarkResult(historyResult)

                                    binding.resultText.text = "$label: ${NumberFormat.getPercentInstance().format(category.score).trim()}"
                                }
                            } else {
                                binding.resultText.text = ""
                            }
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    private fun setTitle(){
        val title = getString(R.string.result)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): ResultViewModel {
        historyResultRepository = HistoryResultRepository(application)
        val factory = ViewModelFactory.getInstance(historyResultRepository)
        return ViewModelProvider(activity, factory)[ResultViewModel::class.java]
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }

}