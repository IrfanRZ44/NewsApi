package id.telkomsel.merchandise.services.numberPicker.listener

import android.util.Log
import id.telkomsel.merchandise.services.numberPicker.enums.ActionEnum
import id.telkomsel.merchandise.services.numberPicker.interfac.ValueChangedListener

class DefaultValueChangedListener : ValueChangedListener {
    override fun valueChanged(value: Int, action: ActionEnum?) {
        val actionText =
            if (action === ActionEnum.MANUAL) "manually set" else if (action === ActionEnum.INCREMENT) "incremented" else "decremented"
        val message =
            String.format("NumberPicker is %s to %d", actionText, value)
        Log.v(this.javaClass.simpleName, message)
    }
}