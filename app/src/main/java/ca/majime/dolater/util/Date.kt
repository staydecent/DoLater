package ca.majime.dolater.util

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalAdjusters.next


fun tomorrow(): LocalDate {
    return LocalDate.now().plusDays(1)
}


fun nextFriday(): LocalDate {
    return LocalDate.now().with(next(DayOfWeek.FRIDAY))
}


fun nextWeek(): LocalDate {
    return LocalDate.now().with(next(DayOfWeek.MONDAY))
}