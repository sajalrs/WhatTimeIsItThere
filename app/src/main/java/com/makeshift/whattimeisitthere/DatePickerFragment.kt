package com.makeshift.whattimeisitthere

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_WHENABOUT = "whenabout"

class DatePickerFragment : DialogFragment() {

    interface Callbacks{
        fun onDateSelected(whenabout: Whenabout)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var whenabout = arguments?.getSerializable(ARG_WHENABOUT) as Whenabout


        val calendar = Calendar.getInstance()
        calendar.time = whenabout.dob;
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener{
                _: DatePicker, year: Int, month: Int, day: Int ->
            val resultDate: Date =GregorianCalendar(year, month, day).time
            whenabout.dob = resultDate
            targetFragment?.let{fragment ->
                (fragment as Callbacks).onDateSelected(whenabout)
            }
        }


        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {
        fun newInstance(whenabout: Whenabout): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WHENABOUT, whenabout)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}