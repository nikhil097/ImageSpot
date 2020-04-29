package com.nikhil.imagespot.ui.base

import android.annotation.SuppressLint
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.nikhil.imagespot.R
import com.nikhil.imagespot.extensions.whenNotNull
import dagger.android.support.DaggerAppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity(), BaseErrorInterface {

    var toolbar: Toolbar? = null

    private var progressDialog: MaterialDialog? = null

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        injectViews()
    }

    protected fun injectViews() {
        toolbar = findViewById(R.id.toolbar)
        setupToolbar()
    }

    protected fun setupToolbar() {
        whenNotNull(toolbar) {
            setSupportActionBar(it)
        }
    }

    fun setActivityTitle(title: String) {
        whenNotNull(supportActionBar) {
            it.title = title
        }
    }

    private fun showProgressDialog() {
        if (!isFinishing) {
            if (progressDialog == null) {
                initMaterialDialog()
            } else if (!progressDialog!!.isShowing) {
                progressDialog!!.show()
            }
        }
    }

    private fun initMaterialDialog() {
        progressDialog = MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .show()
    }

    private fun hideProgressDialog() {
        whenNotNull(progressDialog) {
            progressDialog!!.dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        hideProgressDialog()
    }

    open fun showLoading(active: Boolean) {
        if(active) showProgressDialog() else hideProgressDialog()
    }

    open fun showError(throwable: Throwable) {
        showError(throwable, findViewById(android.R.id.content))
    }

}
