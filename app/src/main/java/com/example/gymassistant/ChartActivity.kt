package com.example.gymassistant

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gymassistant.databinding.ActivityGrafBinding
import com.example.gymassistant.viewmodel.graf.CViewModelGrafDetail
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChartActivity : AppCompatActivity() {
    private var id: String? = null // Идентификатор упражнения
    private lateinit var binding: ActivityGrafBinding
    private val viewModel  : CViewModelGrafDetail by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrafBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        if (!viewModel.initilized.value) {
            intent.extras?.let {bundle ->
                id = bundle.getString(getString(R.string.PARAM_ID))?.let { tempId ->
                    tempId
                }
                id?:run{
                    //Можно сообщение прокрутить через модель представления.
                    Toast.makeText(
                        this@ChartActivity,
                        getString(R.string.INTERNAL_ERROR),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return@let
                }

                //Передаём начальные данные в модель представления.
                viewModel.setItem(
                    id = id!!
                )
            }
        }
        binding.viewModel = viewModel

        val lineChart = binding.lineChart

        val entries1 = mutableListOf<Entry>() //sets_done
        val entries2 = mutableListOf<Entry>() //weight
        val entries3 = mutableListOf<Entry>() //duration
        val entries4 = mutableListOf<Entry>() //times_per_set
        lifecycleScope.launch {
            viewModel.getExercises(id!!).collect { exercisesList ->
                var i = 0.0
                for (exercise in exercisesList) {
                    entries1.add(Entry(i.toFloat(), exercise.sets_done!!.toFloat()))
                    entries2.add(Entry(i.toFloat(), exercise.weight!!.toFloat()))
                    entries3.add(Entry(i.toFloat(), exercise.duration!!.toFloat()))
                    entries4.add(Entry(i.toFloat(), exercise.times_per_set!!.toFloat()))
                    Log.d("MyApp", "" +i+" "+ exercise.sets_done+" "+ exercise.weight +" "+exercise.duration +" "+exercise.times_per_set)
                    i += 1.0
                }
                withContext(Dispatchers.Main) {
                    val dataSet1 = LineDataSet(entries1, "Сделано подходов")
                    val dataSet2 = LineDataSet(entries2, "Вес")
                    val dataSet3 = LineDataSet(entries3, "Продолжительность")
                    val dataSet4 = LineDataSet(entries4, "Повторений в подходе")

                    dataSet1.color = Color.RED
                    dataSet2.color = Color.BLUE
                    dataSet3.color = Color.GREEN
                    dataSet4.color = Color.MAGENTA

                    dataSet1.lineWidth = 2f
                    dataSet2.lineWidth = 2f
                    dataSet3.lineWidth = 2f
                    dataSet4.lineWidth = 2f

                    dataSet1.valueTextSize = 12f
                    dataSet2.valueTextSize = 12f
                    dataSet3.valueTextSize = 12f
                    dataSet4.valueTextSize = 12f

                    dataSet1.valueTextColor = Color.BLACK
                    dataSet2.valueTextColor = Color.BLACK
                    dataSet3.valueTextColor = Color.BLACK
                    dataSet4.valueTextColor = Color.BLACK

                    val lineData = LineData(dataSet1, dataSet2, dataSet3, dataSet4)
                    lineChart.data = lineData

                    // Увеличение шрифта для осей
                    lineChart.xAxis.textSize = 14f
                    lineChart.axisLeft.textSize = 14f
                    lineChart.axisRight.textSize = 14f

                    // Настройка осей
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.position = XAxis.XAxisPosition.BOTTOM

                    val yAxisLeft: YAxis = lineChart.axisLeft
                    yAxisLeft.setDrawGridLines(false)

                    val yAxisRight: YAxis = lineChart.axisRight
                    yAxisRight.isEnabled = false

                    // Увеличение шрифта для легенды (если есть)
                    lineChart.legend.textSize = 14f
                    // Обновляем график
                    lineChart.invalidate()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collect { stringId ->
                    if (stringId < 0)
                        return@collect
                    Toast.makeText(
                        this@ChartActivity,
                        getString(stringId),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}