package com.urfa.ui.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.urfa.R
import com.urfa.ui.base.ReadFileActivity
import com.urfa.ui.weekview.WeekViewEvent
import com.urfa.util.*
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.view_date_picker.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*

class AddUserActivity : ReadFileActivity(), AddUserNavigation {
    private val viewModel: AddUserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        setupView()
        setupListener()
        val startDate = intent.getLongExtra(START_DATE, -1L)
        if (startDate != -1L) {
            val user = viewModel.newUser.value ?: WeekViewEvent()
            user.startTime.timeInMillis = startDate
            user.endTime.timeInMillis = startDate
            user.endTime.add(Calendar.MINUTE, 10)
            viewModel.newUser.postValue(user)
        }
    }

    private fun setupViewModel() {
        viewModel.setNavigation(this)
        viewModel.newUser.observeNonNull(this) {
            addButton.visibility = if(!TextUtils.isEmpty(it.name) && !TextUtils.isEmpty(it.lastName)) View.VISIBLE else View.GONE
            startDateTextView.timeTextView.text = timeFormatter.format(it.startTime.timeInMillis)
            endDateTextView.timeTextView.text = timeFormatter.format(it.endTime.timeInMillis)
            birthDateTextView.timeTextView.text = timeFormatter.format(it.birthDate.timeInMillis)

            startDateTextView.dateTextView.text =
                monthFormatter.format(it.startTime.timeInMillis)
            endDateTextView.dateTextView.text = monthFormatter.format(it.endTime.timeInMillis)
            birthDateTextView.dateTextView.text =
                monthFormatter.format(it.birthDate.timeInMillis)
            fileAttachedTextView.visibility =
                if (TextUtils.isEmpty(it.filePath) || !File(it.filePath).exists()) View.GONE else View.VISIBLE
        }
    }

    private fun setupView() {
        birthDateTextView.pickerHintView.text = getString(R.string.hint_birth_date)
        startDateTextView.pickerHintView.text = getString(R.string.hint_start_date)
        endDateTextView.pickerHintView.text = getString(R.string.hint_end_date)
        surnameEditText.addTextChangedListener(object : MyTextWatcher() {
            override fun afterTextChanged(editor: Editable?) {
                val user: WeekViewEvent = viewModel.newUser.value ?: WeekViewEvent()
                user.name = editor.toString()
                viewModel.newUser.postValue(user)
            }
        })
        nameFanEditText.addTextChangedListener(object : MyTextWatcher() {
            override fun afterTextChanged(editor: Editable?) {
                val user = viewModel.newUser.value ?: WeekViewEvent()
                user.lastName = editor.toString()
                viewModel.newUser.postValue(user)
            }
        })
    }

    private fun setupListener() {
        setupDatePickerListener()
        setupTimePickerListener()
        addButton.setOnDelayedClickListener {
            viewModel.saveUser()
        }
        attachButton.setOnDelayedClickListener {
            showChooserDialog()
        }
        fileAttachedTextView.setOnDelayedClickListener {
            viewModel.newUser.value?.filePath?.let { path ->
                openFromGallery(path)
            }
        }
    }

    override fun onSuccessSavingUser() {
        onError("Success saving")
        this.finish()
    }

    private fun setupDatePickerListener() {
        birthDateTextView.dateTextView.setOnDelayedClickListener {
            getDatePicker(viewModel.getBirthDayCalendar(), birthDatePickListener).show()
        }
        startDateTextView.dateTextView.setOnDelayedClickListener {
            getDatePicker(viewModel.getStartDayCalendar(), startDatePickListener).show()
        }
        endDateTextView.dateTextView.setOnDelayedClickListener {
            getDatePicker(viewModel.getEndDayCalendar(), endDatePickListener).show()
        }
    }

    private fun setupTimePickerListener() {
        birthDateTextView.timeTextView.setOnDelayedClickListener {
            getTimePicker(viewModel.getBirthDayCalendar(), birthTimePickListener).show()
        }
        startDateTextView.timeTextView.setOnDelayedClickListener {
            getTimePicker(viewModel.getStartDayCalendar(), startTimePickListener).show()
        }
        endDateTextView.timeTextView.setOnDelayedClickListener {
            getTimePicker(viewModel.getEndDayCalendar(), endTimePickListener).show()
        }
    }

    private fun getDatePicker(
        calendar: Calendar,
        listener: DatePickerDialog.OnDateSetListener
    ): DatePickerDialog {
        val dialog = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun getTimePicker(
        calendar: Calendar,
        listener: TimePickerDialog.OnTimeSetListener
    ): TimePickerDialog {
        val dialog = TimePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            listener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private val birthDatePickListener =
        DatePickerDialog.OnDateSetListener { picker, year, month, day ->
            val user = viewModel.newUser.value ?: WeekViewEvent()
            user.birthDate.set(Calendar.YEAR, year)
            user.birthDate.set(Calendar.MONTH, month)
            user.birthDate.set(Calendar.DAY_OF_MONTH, day)
            viewModel.newUser.postValue(user)
        }

    private val birthTimePickListener =
        TimePickerDialog.OnTimeSetListener { picker, hour, minute ->
            val user = viewModel.newUser.value ?: WeekViewEvent()
            user.birthDate.set(Calendar.HOUR_OF_DAY, hour)
            user.birthDate.set(Calendar.MINUTE, minute)
            viewModel.newUser.postValue(user)
        }

    private val startDatePickListener =
        DatePickerDialog.OnDateSetListener { picker, year, month, day ->
            val user = viewModel.newUser.value ?: WeekViewEvent()
            user.startTime.set(Calendar.YEAR, year)
            user.startTime.set(Calendar.MONTH, month)
            user.startTime.set(Calendar.DAY_OF_MONTH, day)
            viewModel.newUser.postValue(user)
        }

    private val startTimePickListener =
        TimePickerDialog.OnTimeSetListener { picker, hour, minute ->
            val user = viewModel.newUser.value ?: WeekViewEvent()
            user.startTime.set(Calendar.HOUR_OF_DAY, hour)
            user.startTime.set(Calendar.MINUTE, minute)
            viewModel.newUser.postValue(user)
        }

    private val endDatePickListener =
        DatePickerDialog.OnDateSetListener { picker, year, month, day ->
            val user = viewModel.newUser.value ?: WeekViewEvent()
            user.endTime.set(Calendar.YEAR, year)
            user.endTime.set(Calendar.MONTH, month)
            user.endTime.set(Calendar.DAY_OF_MONTH, day)
            viewModel.newUser.postValue(user)
        }

    private val endTimePickListener =
        TimePickerDialog.OnTimeSetListener { picker, hour, minute ->
            val user = viewModel.newUser.value ?: WeekViewEvent()
            user.endTime.set(Calendar.HOUR_OF_DAY, hour)
            user.endTime.set(Calendar.MINUTE, minute)
            viewModel.newUser.postValue(user)
        }

    override fun onReceiveImageFile(path: String) {
        viewModel.addUri(path)
    }

    companion object {
        fun newInstance(context: Context, startDate: Calendar): Intent {
            return Intent(context, AddUserActivity::class.java).apply {
                putExtra(START_DATE, startDate.timeInMillis)
            }
        }

        private val START_DATE = "startdate"
    }
}