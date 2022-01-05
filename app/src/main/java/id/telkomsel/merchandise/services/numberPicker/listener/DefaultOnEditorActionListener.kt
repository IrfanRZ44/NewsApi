package id.telkomsel.merchandise.services.numberPicker.listener

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import id.telkomsel.merchandise.services.numberPicker.enums.ActionEnum
import id.telkomsel.merchandise.services.numberPicker.NumberPicker

class DefaultOnEditorActionListener(var layout: NumberPicker) :
    OnEditorActionListener {
    override fun onEditorAction(
        v: TextView,
        actionId: Int,
        event: KeyEvent
    ): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            try {
                val value = v.text.toString().toInt()
                layout.value = value
                if (layout.value == value) {
                    layout.valueChangedListener?.valueChanged(value, ActionEnum.MANUAL)
                    return false
                }
            } catch (e: NumberFormatException) {
                layout.refresh()
            }
        }
        return true
    }

}