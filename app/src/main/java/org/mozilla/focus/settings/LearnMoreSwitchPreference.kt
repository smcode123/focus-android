package org.mozilla.focus.settings

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.preference.PreferenceViewHolder
import android.support.v7.preference.SwitchPreferenceCompat
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import mozilla.components.browser.session.Session
import org.mozilla.focus.R
import org.mozilla.focus.ext.components

abstract class LearnMoreSwitchPreference(context: Context?, attrs: AttributeSet?) :
    SwitchPreferenceCompat(context, attrs) {

    init {
        layoutResource = R.layout.preference_switch_learn_more
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)

        getDescription()?.let {
            val summaryView = holder!!.findViewById(android.R.id.summary) as TextView
            summaryView.text = it
            summaryView.visibility = View.VISIBLE
        }

        val learnMoreLink = holder!!.findViewById(R.id.link) as TextView
        learnMoreLink.paintFlags = learnMoreLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        learnMoreLink.setTextColor(ContextCompat.getColor(context, R.color.focusContrastPink))
        learnMoreLink.setOnClickListener {
            // This is a hardcoded link: if we ever end up needing more of these links, we should
            // move the link into an xml parameter, but there's no advantage to making it configurable now.
            val session = Session(getLearnMoreUrl(), source = Session.Source.MENU)
            context.components.sessionManager.add(session, selected = true)
            if (context is ContextThemeWrapper) {
                if ((context as ContextThemeWrapper).baseContext is Activity) {
                    ((context as ContextThemeWrapper).baseContext as Activity).finish()
                }
            } else {
                (context as? Activity)?.finish()
            }
        }

        val backgroundDrawableArray =
            context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        val backgroundDrawable = backgroundDrawableArray.getDrawable(0)
        backgroundDrawableArray.recycle()
        learnMoreLink.background = backgroundDrawable
    }

    open fun getDescription(): String? = null

    abstract fun getLearnMoreUrl(): String
}
