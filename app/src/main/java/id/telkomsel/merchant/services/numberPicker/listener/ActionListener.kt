package id.telkomsel.merchant.services.numberPicker.listener

import android.view.View
import android.widget.EditText
import id.telkomsel.merchant.services.numberPicker.enums.ActionEnum
import id.telkomsel.merchant.services.numberPicker.NumberPicker

class ActionListener(
    var layout: NumberPicker,
    var display: EditText,
    var action: ActionEnum
) : View.OnClickListener {
    override fun onClick(v: View) {
        try {
            val newValue = display.text.toString().toInt()
            if (!layout.valueIsAllowed(newValue)) {
                return
            }
            layout.value = newValue
        } catch (e: NumberFormatException) {
            layout.refresh()
        }
        when (action) {
            ActionEnum.INCREMENT -> layout.increment()
            ActionEnum.DECREMENT -> layout.decrement()
        }
    }

}