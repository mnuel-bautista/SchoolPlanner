package dev.manuel.timetable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReminderReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "ReminderReceiver", Toast.LENGTH_SHORT)
            .show()
    }

}