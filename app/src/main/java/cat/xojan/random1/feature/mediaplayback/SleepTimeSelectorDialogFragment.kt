package cat.xojan.random1.feature.mediaplayback

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import cat.xojan.random1.R


class SleepTimeSelectorDialogFragment: DialogFragment() {

    companion object {
        val TAG = SleepTimeSelectorDialogFragment::class.java.simpleName
    }

    interface Listener {
        fun onTimeSelected(milliseconds: Long)
    }

    private lateinit var listener: Listener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as MediaPlaybackFullScreenActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity as Context)
                .setItems(R.array.sleep_timer_options, { _, which ->
                    when (which) {
                        0 -> listener.onTimeSelected(0)
                        1 -> listener.onTimeSelected(300000)
                        2 -> listener.onTimeSelected(600000)
                        3 -> listener.onTimeSelected(900000)
                        4 -> listener.onTimeSelected(1200000)
                        5 -> listener.onTimeSelected(1800000)
                        6 -> listener.onTimeSelected(2700000)
                        7 -> listener.onTimeSelected(3600000)
                    }
                })
        return builder.create()
    }
}