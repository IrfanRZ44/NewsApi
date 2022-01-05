package id.telkomsel.merchandise.services.numberPicker.listener

import android.util.Log
import id.telkomsel.merchandise.services.numberPicker.interfac.LimitExceededListener

class DefaultLimitExceededListener : LimitExceededListener {
    override fun limitExceeded(limit: Int, exceededValue: Int) {
        val message = String.format(
            "NumberPicker cannot set to %d because the limit is %d.",
            exceededValue,
            limit
        )
        Log.v(this.javaClass.simpleName, message)
    }
}