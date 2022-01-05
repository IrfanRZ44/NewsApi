package id.telkomsel.merchandise.services.numberPicker.interfac

interface LimitExceededListener {
    fun limitExceeded(limit: Int, exceededValue: Int)
}