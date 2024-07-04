package com.example.lutongph.presentation.calendar

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.presentation.calendar.CalendarAdapter.OnItemListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.database.Cursor
import android.widget.EditText
import android.widget.ArrayAdapter
import com.example.lutongph.util.DBHelper
import com.example.lutongph.data.model.EventList
import com.example.lutongph.presentation.login.Login
import com.example.lutongph.R
import com.example.lutongph.constant.AppConstant.CHANNEL_ID
import com.example.lutongph.constant.AppConstant.MESSAGE_EXTRA
import com.example.lutongph.constant.AppConstant.NOTIFICATION_ID
import com.example.lutongph.constant.AppConstant.TITLE_EXTRA
import com.example.lutongph.data.model.RecipeList
import com.example.lutongph.presentation.notification.Notification
import java.time.DayOfWeek
import java.util.Calendar

class Calendar : Fragment(), OnItemListener {
    private lateinit var db: DBHelper
    private lateinit var monthYear: TextView
    private lateinit var calendarview: RecyclerView
    private lateinit var r_array: ArrayList<RecipeList>
    private lateinit var array: ArrayList<EventList>
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventadapter: EventRecyclerAdapter
    private var sHour: Int = 0
    private var sMinute: Int = 0
    var sql: String = ""
    val userID: Int = Login.userID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        creteNotificationChannel(requireContext())

        monthYear = view.findViewById(R.id.month)
        calendarview = view.findViewById(R.id.calendar_recyclerview)
        sdate = LocalDate.now()

        array = ArrayList()
        recyclerView = view.findViewById(R.id.event_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Updated this line
        recyclerView.setHasFixedSize(true)
        eventadapter = EventRecyclerAdapter(array)
        recyclerView.adapter = eventadapter

        db = DBHelper(view.context)
        initsearchcontent()
        setWeek()
        updateEvent()

        val nextAction = view.findViewById<ImageButton>(R.id.btnNext)
        nextAction.setOnClickListener {
            sdate = sdate.plusWeeks(1)
            setWeek()
            updateEvent()
        }

        val prevAction = view.findViewById<ImageButton>(R.id.btnBack)
        prevAction.setOnClickListener {
            sdate = sdate.minusWeeks(1)
            setWeek()
            updateEvent()
        }

        val addEvent = view.findViewById<Button>(R.id.addEvent)
        addEvent.setOnClickListener {
            showAddEventDialog(sdate)
        }

        return view

    }

    private fun creteNotificationChannel(context: Context) {
        val name = "LutongPH"
        val desc = "LutongPH Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun initsearchcontent() {

        var newcursor: Cursor? = db!!.getrecipe()
        r_array = ArrayList()
        while (newcursor!!.moveToNext()) {
            val foodid = newcursor.getInt(0)
            val foodname = newcursor.getString(1)
            val foodprocedure = newcursor.getString(2)
            val preptime = newcursor.getString(3)
            val foodingredients = newcursor.getString(4)
            val foodcost = newcursor.getString(5)
            val fooddesc = newcursor.getString(6)
            val foodimage = "https://drive.google.com/uc?id=" + newcursor.getString(7)
            r_array.add(
                RecipeList(
                    foodid,
                    foodname,
                    foodprocedure,
                    preptime,
                    foodingredients,
                    foodcost,
                    fooddesc,
                    foodimage
                )
            )
        }
        newcursor.close()
    }

    private fun displayEvents() {
        var selecteddate = sdate.toString()
        sql =
            "SELECT r.FoodName, c.EventTime, c.EventName FROM calendar c INNER JOIN recipes r ON c.FoodID = r.FoodID INNER JOIN userdata u ON c.User_ID = u.User_ID WHERE u.User_ID=$userID AND c.EventDate='$selecteddate' AND c.Status='Pending'"
        var newcursor: Cursor? = db!!.display_calendardata(sql)
        array = ArrayList()
        while (newcursor!!.moveToNext()) {
            val food = newcursor.getString(0)
            val time = newcursor.getString(1)
            val title = newcursor.getString(2)
            array.add(EventList(food, title, time))
        }
        println(array)
        newcursor.close()
    }

    private fun setWeek() {
        monthYear.setText(getDate(sdate))
        val dWeek: ArrayList<LocalDate> = daysArray(sdate)
        val calendarAdapter = CalendarAdapter(dWeek, this)
        val layoutManager = GridLayoutManager(requireContext(), 7)
        calendarview.layoutManager = layoutManager
        calendarview.setHasFixedSize(true)
        calendarview.setAdapter(calendarAdapter)
    }

    private fun daysArray(date: LocalDate): ArrayList<LocalDate> {
        val days = ArrayList<LocalDate>()
        var current = sundayForDate(date)
        val endDate = current?.plusWeeks(1)

        while (current!!.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    private fun sundayForDate(current: LocalDate): LocalDate? {
        val oneWeekAgo = current.minusWeeks(1)
        var tempDate = current
        while (tempDate.isAfter(oneWeekAgo)) {
            if (tempDate.dayOfWeek == DayOfWeek.SUNDAY)
                return tempDate

            tempDate = tempDate.minusDays(1)
        }

        return null
    }

    private fun getDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun showAddEventDialog(selectedDate: LocalDate) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_event, null)

        builder.setView(dialogView)

        val addEventButton = dialogView.findViewById<Button>(R.id.add_event_dialog_button)
        val eventName = dialogView.findViewById<EditText>(R.id.event_name)
        val eventTime = dialogView.findViewById<EditText>(R.id.event_time)
        val eventFood = dialogView.findViewById<AutoCompleteTextView>(R.id.event_food)

        val recipes = r_array.map { it.food_name }
        val autoCompleteAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, recipes)

        eventTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)


            val timePickerDialog =
                TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                    sHour = selectedHour
                    sMinute = selectedMinute
                    val formattedHour = String.format("%02d", sHour)
                    val formattedMinute = String.format("%02d", sMinute)
                    eventTime.setText("$formattedHour:$formattedMinute")
                }, hourOfDay, minute, false)

            timePickerDialog.show()
        }

        eventFood.setAdapter(autoCompleteAdapter)
        eventFood.threshold = 1

        val alertDialog = builder.create()
        alertDialog.show()

        addEventButton.setOnClickListener {
            if (eventName.text.isNotEmpty() && eventFood.text.isNotEmpty() && eventTime.text.isNotEmpty()) {
                val time = eventTime.text.toString()
                val date = sdate.toString()
                val title = eventName.text.toString()
                val food = eventFood.text.toString()
                val fid = db.get_foodid(food)
                val uid = userID
                val status = "Pending"
                val savedata = db.insert_calendardata(title, date, time, status, uid, fid)
                if (savedata == true) {
                    Toast.makeText(requireContext(), "Event added", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    scheduleNotification(requireContext(), food)
                    updateEvent()
                }
            } else {
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(context: Context, dish: String) {
        val title = "LutongPH"
        val message = "It's cooking time! $dish won't cook by itself"

        val intent = Intent(requireContext(), Notification::class.java).apply {
            putExtra(TITLE_EXTRA, title)
            putExtra(MESSAGE_EXTRA, message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager


        val time = getTime()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun getTime(): Long{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, sdate.year)
        calendar.set(Calendar.MONTH, sdate.monthValue - 1)
        calendar.set(Calendar.DAY_OF_MONTH, sdate.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, sHour)
        calendar.set(Calendar.MINUTE, sMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    private fun updateEvent() {
        displayEvents()
        eventadapter = EventRecyclerAdapter(array)
        recyclerView.adapter = eventadapter

        eventadapter.setRemoveOnItemClickListener(object :
            EventRecyclerAdapter.RemoveOnItemClickListener {
            override fun onItemClick(position: Int) {
                val event = array[position]
                val eventTime = event.event_time
                val status = "Deleted"
                val userID = userID
                val eventDate = sdate.toString()

                db.updatedata(status, userID, eventDate, eventTime)

                println(position)
                updateEvent()
            }

        })

        recyclerView.adapter = eventadapter
    }

    override fun onItemClick(position: Int, date: LocalDate) {
        if (date != null) {
            sdate = date
            setWeek()
            updateEvent()
        }
    }

    companion object {
        lateinit var sdate: LocalDate
    }
}