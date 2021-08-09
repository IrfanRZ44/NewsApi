package id.telkomsel.merchant.services.numberPicker.interfac

interface LimitExceededListener {
    fun limitExceeded(limit: Int, exceededValue: Int)
}