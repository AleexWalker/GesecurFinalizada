package com.gesecur.app.ui.vigilant.services.error

import android.widget.Toast

open class ErrorAction {
    class showToastError(val string: String) : ErrorAction()
}