package id.telkomsel.merchant.services.numberPicker.listener

import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import id.telkomsel.merchant.services.numberPicker.enums.ActionEnum
import id.telkomsel.merchant.services.numberPicker.NumberPicker

class DefaultOnFocusChangeListener(var layout: NumberPicker) :
    OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        val editText = v as EditText
        if (!hasFocus) {
            try {
                val value = editText.text.toString().toInt()
                layout.value = value
                if (layout.value == value) {
                    layout.valueChangedListener?.valueChanged(value, ActionEnum.MANUAL)
                } else {
                    layout.refresh()
                }
            } catch (e: NumberFormatException) {
                layout.refresh()
            }
        }
    }

}