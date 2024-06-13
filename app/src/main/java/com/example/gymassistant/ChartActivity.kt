package com.example.gymassistant

import android.graphics.Color
import android.os.Bundle
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
import kotlinx.coroutines.launch

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

        val entries = mutableListOf<Entry>()


        // Добавляем точки на график (пример данных)
        entries.add(Entry(0f, 1f))
        entries.add(Entry(1f, 2f))
        entries.add(Entry(2f, 0f))
        entries.add(Entry(3f, 4f))
        entries.add(Entry(4f, 3f))

        val dataSet = LineDataSet(entries, "Пример данных")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Настройка осей
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val yAxisLeft: YAxis = lineChart.axisLeft
        yAxisLeft.setDrawGridLines(false)

        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false

        // Обновляем график
        lineChart.invalidate()

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