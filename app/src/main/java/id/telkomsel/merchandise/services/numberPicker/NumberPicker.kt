package id.telkomsel.merchandise.services.numberPicker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.services.numberPicker.enums.ActionEnum
import id.telkomsel.merchandise.services.numberPicker.interfac.LimitExceededListener
import id.telkomsel.merchandise.services.numberPicker.interfac.ValueChangedListener
import id.telkomsel.merchandise.services.numberPicker.listener.*

class NumberPicker : LinearLayout {
    // default values
    private val DEFAULT_MIN = 0
    private val DEFAULT_MAX = 999999
    private val DEFAULT_VALUE = 1
    private val DEFAULT_UNIT = 1
    private val DEFAULT_LAYOUT = R.layout.number_picker_layout
    private val DEFAULT_FOCUSABLE = false

    // required variables
    var min = 0
    var max = 0
    var unit = 0
    private var currentValue = 0
    private var layout = 0

    // ui components
    private var mContext: Context? = null
    private var decrementButton: Button? = null
    private var incrementButton: Button? = null
    private var displayEditText: EditText? = null

    // listeners
    var limitExceededListener: LimitExceededListener? = null
    var valueChangedListener: ValueChangedListener? = null

    constructor(context: Context?) : super(context, null) {}
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    private fun initialize(
        context: Context,
        attrs: AttributeSet?
    ) {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.NumberPicker, 0, 0)

        // set required variables with values of xml layout attributes or default ones
        min = attributes.getInteger(R.styleable.NumberPicker_min, DEFAULT_MIN)
        max = attributes.getInteger(R.styleable.NumberPicker_max, DEFAULT_MAX)
        currentValue =
            attributes.getInteger(R.styleable.NumberPicker_value, DEFAULT_VALUE)
        unit = attributes.getInteger(R.styleable.NumberPicker_unit, DEFAULT_UNIT)
        layout =
            attributes.getResourceId(R.styleable.NumberPicker_custom_layout, DEFAULT_LAYOUT)
        val focusable =
            attributes.getBoolean(R.styleable.NumberPicker_focusable, DEFAULT_FOCUSABLE)
        mContext = context

        // if current value is greater than the max. value, decrement it to the max. value
        currentValue = if (currentValue > max) max else currentValue

        // if current value is less than the min. value, decrement it to the min. value
        currentValue = if (currentValue < min) min else currentValue

        // set layout view
        LayoutInflater.from(mContext).inflate(layout, this, true)

        // init ui components
        decrementButton =
            findViewById<View>(R.id.decrement) as Button
        incrementButton =
            findViewById<View>(R.id.increment) as Button
        displayEditText = findViewById<View>(R.id.display) as EditText

        // register button click and action listeners
        incrementButton!!.setOnClickListener(
            ActionListener(
                this,
                displayEditText!!,
                ActionEnum.INCREMENT
            )
        )
        decrementButton!!.setOnClickListener(
            ActionListener(
                this,
                displayEditText!!,
                ActionEnum.DECREMENT
            )
        )

        // init listener for exceeding upper and lower limits
        limitExceededListener = DefaultLimitExceededListener()
        // init listener for increment&decrement
        valueChangedListener = DefaultValueChangedListener()
        // init listener for focus change
        this.onFocusChangeListener = DefaultOnFocusChangeListener(this)
        // init listener for done action in keyboard
        setOnEditorActionListener(DefaultOnEditorActionListener(this))

        // set default display mode
        setDisplayFocusable(focusable)

        // update ui view
        refresh()
    }

    fun refresh() {
        displayEditText!!.setText(Integer.toString(currentValue))
    }

    override fun clearFocus() {
        displayEditText!!.clearFocus()
    }

    fun valueIsAllowed(value: Int): Boolean {
        return value >= min && value <= max
    }

    var value: Int
        get() = currentValue
        set(value) {
            if (!valueIsAllowed(value)) {
                limitExceededListener!!.limitExceeded(
                    if (value < min) min else max,
                    value
                )
                return
            }
            currentValue = value
            refresh()
        }

    fun setOnEditorActionListener(onEditorActionListener: OnEditorActionListener?) {
        displayEditText!!.setOnEditorActionListener(onEditorActionListener)
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener) {
        displayEditText!!.onFocusChangeListener = onFocusChangeListener
    }

    fun setActionEnabled(action: ActionEnum, enabled: Boolean) {
        if (action === ActionEnum.INCREMENT) {
            incrementButton!!.isEnabled = enabled
        } else if (action === ActionEnum.DECREMENT) {
            decrementButton!!.isEnabled = enabled
        }
    }

    fun setDisplayFocusable(focusable: Boolean) {
        displayEditText!!.isFocusable = focusable

        // required for making EditText focusable
        if (focusable) {
            displayEditText!!.isFocusableInTouchMode = true
        }
    }

    fun increment() {
        changeValueBy(unit)
    }

    fun increment(unit: Int) {
        changeValueBy(unit)
    }

    fun decrement() {
        changeValueBy(-unit)
    }

    fun decrement(unit: Int) {
        changeValueBy(-unit)
    }

    private fun changeValueBy(unit: Int) {
        val oldValue = value
        value = currentValue + unit
        if (oldValue != value) {
            valueChangedListener!!.valueChanged(
                value,
                if (unit > 0) ActionEnum.INCREMENT else ActionEnum.DECREMENT
            )
        }
    }
}