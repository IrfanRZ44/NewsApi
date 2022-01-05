package id.telkomsel.merchandise.services.numberPicker.interfac

import id.telkomsel.merchandise.services.numberPicker.enums.ActionEnum

interface ValueChangedListener {
    fun valueChanged(value: Int, action: ActionEnum?)
}