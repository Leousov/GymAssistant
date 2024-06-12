package com.example.gymassistant

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Адаптер для установки значения в TextInputEditText из MutableStateFlow
@BindingAdapter("mutableStateFlowText")
fun setMutableStateFlowText(view: TextInputEditText, stateFlow: MutableStateFlow<Int>?) {
    stateFlow?.let { flow ->
        val lifecycleOwner = view.context as? LifecycleOwner ?: return

        lifecycleOwner.lifecycleScope.launch {
            flow.collectLatest { value ->
                if (view.text.toString() != value.toString()) {
                    view.setText(value.toString())
                }
            }
        }
    }
}

// Адаптер для получения значения из TextInputEditText и обновления MutableStateFlow
@BindingAdapter("mutableStateFlowTextAttrChanged")
fun setMutableStateFlowTextListener(view: TextInputEditText, listener: InverseBindingListener?) {
    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            listener?.onChange()
        }
    })
}

// Адаптер для двусторонней привязки
@InverseBindingAdapter(attribute = "mutableStateFlowText")
fun getMutableStateFlowText(view: TextInputEditText): Int {
    return view.text.toString().toIntOrNull() ?: -1
}


