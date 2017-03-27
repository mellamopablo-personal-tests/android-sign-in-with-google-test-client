package io.github.mellamopablo.signinwithgoogletest.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import io.github.mellamopablo.signinwithgoogletest.R

class LogView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var log: TextView? = null

    init {
        val thisView = View.inflate(context, R.layout.component_log_view, this)
        log = thisView.findViewById(R.id.component_log_view_log) as TextView
    }

    constructor(context: Context) : this(context, null)

    public fun clear() {
        log?.text = ""
    }

    public fun addLine(line: String) {
        log?.text = if (log?.text == "") line else "${log?.text}\n$line"
    }

}