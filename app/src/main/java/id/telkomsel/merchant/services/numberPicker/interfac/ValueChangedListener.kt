package id.telkomsel.merchant.services.numberPicker.interfac

import id.telkomsel.merchant.services.numberPicker.enums.ActionEnum

interface ValueChangedListener {
    fun valueChanged(value: Int, action: ActionEnum?)
}