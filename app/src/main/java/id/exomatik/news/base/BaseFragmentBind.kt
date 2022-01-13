package id.exomatik.news.base

import android.view.View
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseFragmentBind<B : ViewDataBinding> : Fragment() {
    protected lateinit var bind: B
    protected abstract fun getLayoutResource(): Int
    protected abstract fun myCodeHere()
    protected var savedInstanceState: Bundle? = null
    protected var supportActionBar : ActionBar? = null

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        supportActionBar = (activity as AppCompatActivity).supportActionBar
        bind = DataBindingUtil.inflate(layoutInflater, getLayoutResource(), paramViewGroup, false)

        savedInstanceState = paramBundle
        myCodeHere()

        return bind.root
    }
}