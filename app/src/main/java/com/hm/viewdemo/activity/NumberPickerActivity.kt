package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.FiveItemWheelPicker

/**
 * NumberPicker 的基础用法示例：时分选择、格式化显示、监听值变化和循环滚动。
 */
class NumberPickerActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, NumberPickerActivity::class.java))
        }
    }

    private val defaultHour = 9
    private val defaultMinute = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_picker)

        val hourPicker = findViewById<FiveItemWheelPicker>(R.id.hour_picker)
        val minutePicker = findViewById<FiveItemWheelPicker>(R.id.minute_picker)
        val selectedValue = findViewById<TextView>(R.id.tv_selected_value)
        val wrapSwitch = findViewById<SwitchCompat>(R.id.switch_wrap_selector)
        val resetButton = findViewById<Button>(R.id.btn_reset_number_picker)

        configurePicker(hourPicker, 0, 23, defaultHour)
        configurePicker(minutePicker, 0, 59, defaultMinute)
        updateSelectedTime(selectedValue, defaultHour, defaultMinute)

        hourPicker.setOnValueChangedListener { _, _, newValue ->
            updateSelectedTime(selectedValue, newValue, minutePicker.value)
        }
        minutePicker.setOnValueChangedListener { _, _, newValue ->
            updateSelectedTime(selectedValue, hourPicker.value, newValue)
        }

        wrapSwitch.setOnCheckedChangeListener { _, isChecked ->
            hourPicker.wrapSelectorWheel = isChecked
            minutePicker.wrapSelectorWheel = isChecked
        }

        resetButton.setOnClickListener {
            hourPicker.value = defaultHour
            minutePicker.value = defaultMinute
            updateSelectedTime(selectedValue, defaultHour, defaultMinute)
        }
    }

    private fun configurePicker(picker: FiveItemWheelPicker, minValue: Int, maxValue: Int, value: Int) {
        picker.minValue = minValue
        picker.maxValue = maxValue
        picker.value = value
        picker.wrapSelectorWheel = true
        picker.formatter = { selectedValue -> "%02d".format(selectedValue) }
    }

    private fun updateSelectedTime(textView: TextView, hour: Int, minute: Int) {
        textView.text = getString(R.string.number_picker_selected_value, hour, minute)
    }

}
