package com.urfa.ui.weekview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.urfa.db.dao.BaseEntity
import com.urfa.ui.weekview.WeekViewUtil.isSameDay
import java.util.*

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
@Entity(
    tableName = "user",
    indices = [
        Index("id")
    ]
)
class WeekViewEvent : BaseEntity<Long> {
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0
    @ColumnInfo(name = "username")
    var name: String = ""
    @ColumnInfo(name = "surname")
    var lastName: String = ""
    var location: String? = null
    var color: Int = 0
    var isAllDay: Boolean = false
    @ColumnInfo(name = "birth_date")
    var birthDate: Calendar = Calendar.getInstance()
    @ColumnInfo(name = "start_date")
    var startTime: Calendar = Calendar.getInstance()
    @ColumnInfo(name = "end_date")
    var endTime: Calendar = getInitialEndDate()
    var filePath: String? = null

    private fun getInitialEndDate() : Calendar {
        val date = Calendar.getInstance()
        date.add(Calendar.MINUTE, 10)
        return date
    }

    constructor() {

    }

    /**
     * Initializes the event for week view.
     * @param id The id of the event.
     * @param name Name of the event.
     * @param startYear Year when the event starts.
     * @param startMonth Month when the event starts.
     * @param startDay Day when the event starts.
     * @param startHour Hour (in 24-hour format) when the event starts.
     * @param startMinute Minute when the event starts.
     * @param endYear Year when the event ends.
     * @param endMonth Month when the event ends.
     * @param endDay Day when the event ends.
     * @param endHour Hour (in 24-hour format) when the event ends.
     * @param endMinute Minute when the event ends.
     */
    constructor(
        id: Long,
        name: String,
        startYear: Int,
        startMonth: Int,
        startDay: Int,
        startHour: Int,
        startMinute: Int,
        endYear: Int,
        endMonth: Int,
        endDay: Int,
        endHour: Int,
        endMinute: Int,
        path: String?
    ) {
        this.id = id

        this.startTime = Calendar.getInstance()
        this.startTime.set(Calendar.YEAR, startYear)
        this.startTime.set(Calendar.MONTH, startMonth - 1)
        this.startTime.set(Calendar.DAY_OF_MONTH, startDay)
        this.startTime.set(Calendar.HOUR_OF_DAY, startHour)
        this.startTime.set(Calendar.MINUTE, startMinute)

        this.endTime = Calendar.getInstance()
        this.endTime.set(Calendar.YEAR, endYear)
        this.endTime.set(Calendar.MONTH, endMonth - 1)
        this.endTime.set(Calendar.DAY_OF_MONTH, endDay)
        this.endTime.set(Calendar.HOUR_OF_DAY, endHour)
        this.endTime.set(Calendar.MINUTE, endMinute)

        this.name = name
        this.filePath = path
    }

    /**
     * Initializes the event for week view.
     * @param id The id of the event.
     * @param name Name of the event.
     * @param location The location of the event.
     * @param startTime The time when the event starts.
     * @param endTime The time when the event ends.
     * @param allDay Is the event an all day event.
     */
    @JvmOverloads
    constructor(
        id: Long,
        name: String,
        lastName: String,
        location: String?,
        color: Int,
        isAllDay: Boolean,
        birthDate: Calendar,
        startTime: Calendar,
        endTime: Calendar,
        path : String?
    ) {
        this.id = id
        this.name = name
        this.lastName = lastName
        this.location = location
        this.color = color
        this.isAllDay = isAllDay
        this.birthDate = birthDate
        this.startTime = startTime
        this.endTime = endTime
        this.filePath = path
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as WeekViewEvent?

        return id == that!!.id

    }

    override fun hashCode(): Int {
        return (id xor id.ushr(32)).toInt()
    }

    fun splitWeekViewEvents(): List<WeekViewEvent> {
        //This function splits the WeekViewEvent in WeekViewEvents by day
        val events = ArrayList<WeekViewEvent>()
        // The first millisecond of the next day is still the same day. (no need to split events for this).
        var endTime = this.endTime.clone() as Calendar
        endTime.add(Calendar.MILLISECOND, -1)
        if (!isSameDay(this.startTime, endTime)) {
            endTime = this.startTime.clone() as Calendar
            endTime.set(Calendar.HOUR_OF_DAY, 23)
            endTime.set(Calendar.MINUTE, 59)
            val event1 = WeekViewEvent(
                this.id,
                this.name,
                this.lastName,
                this.location,
                this.color,
                this.isAllDay,
                this.birthDate,
                this.startTime,
                endTime,
                this.filePath
            )
            event1.color = this.color
            events.add(event1)

            // Add other days.
            val otherDay = this.startTime.clone() as Calendar
            otherDay.add(Calendar.DATE, 1)
            while (!isSameDay(otherDay, this.endTime)) {
                val overDay = otherDay.clone() as Calendar
                overDay.set(Calendar.HOUR_OF_DAY, 0)
                overDay.set(Calendar.MINUTE, 0)
                val endOfOverDay = overDay.clone() as Calendar
                endOfOverDay.set(Calendar.HOUR_OF_DAY, 23)
                endOfOverDay.set(Calendar.MINUTE, 59)
                val eventMore =
                    WeekViewEvent(
                        this.id,
                        this.name,
                        this.lastName,
                        this.location,
                        this.color,
                        false,
                        this.birthDate,
                        overDay,
                        endOfOverDay,
                        this.filePath
                    )
                eventMore.color = this.color
                events.add(eventMore)

                // Add next day.
                otherDay.add(Calendar.DATE, 1)
            }

            // Add last day.
            val startTime = this.endTime.clone() as Calendar
            startTime.set(Calendar.HOUR_OF_DAY, 0)
            startTime.set(Calendar.MINUTE, 0)
            val event2 = WeekViewEvent(
                this.id,
                this.name,
                this.lastName,
                this.location,
                this.color,
                this.isAllDay,
                this.birthDate,
                startTime,
                this.endTime,
                this.filePath
            )
            event2.color = this.color
            events.add(event2)
        } else {
            events.add(this)
        }

        return events
    }
}
/**
 * Initializes the event for week view.
 * @param id The id of the event.
 * @param name Name of the event.
 * @param location The location of the event.
 * @param startTime The time when the event starts.
 * @param endTime The time when the event ends.
 */
