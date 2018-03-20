package ca.majime.dolater.util

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.TemporalAdjusters.next


fun tomorrow(date: LocalDateTime?): LocalDateTime {
    val d: LocalDateTime = date ?: LocalDate.now().atTime(0, 0)
    return d.plusDays(1)
}


fun nextFriday(date: LocalDateTime?): LocalDateTime {
    val d: LocalDateTime = date ?: LocalDate.now().atTime(0, 0)
    return d.with(next(DayOfWeek.FRIDAY))
}


fun nextWeek(date: LocalDateTime?): LocalDateTime {
    val d: LocalDateTime = date ?: LocalDate.now().atTime(0, 0)
    return d.with(next(DayOfWeek.MONDAY))
}


fun setHour(date: LocalDateTime?, hour: Int) : LocalDateTime {
    val d: LocalDateTime = date ?: LocalDate.now().atTime(0, 0)
    return d.withHour(hour)
}
