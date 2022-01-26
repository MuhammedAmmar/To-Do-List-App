package com.moworks.todolist
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import java.util.*


fun  createTimePickerDialog(context: Context , listener : TimePickerDialog.OnTimeSetListener) : TimePickerDialog{

    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR]
    val minute = calendar[Calendar.MINUTE]
    return TimePickerDialog(context, listener , hour, minute, false)
}



 fun createDatePickerDialog(context: Context , listener : DatePickerDialog.OnDateSetListener) : DatePickerDialog {
    val calendar = Calendar.getInstance()
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val day = calendar[Calendar.DAY_OF_MONTH]
   return  DatePickerDialog(context , listener, year,month, day)
}


fun createDialog (context: Context, positiveListener: DialogInterface.OnClickListener, cancelListener: DialogInterface.OnClickListener ) :AlertDialog {
    return   AlertDialog.Builder(context).setTitle(context.getString(R.string.dialog_title)) .setMessage(context.getString(R.string.dialog_message))
            .setNegativeButton(context.getString(R.string.dialog_action_cancel), cancelListener )
            .setPositiveButton(context.getString(R.string.dialog_action_yes) , positiveListener).create()
}

