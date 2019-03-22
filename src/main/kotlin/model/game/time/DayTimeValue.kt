package model.game.time;

enum class DayTimeValue(val startTime: Int) {
	Night(0),
	Morning(4),
	Day(10),
	Evening(18);

    companion object {
        fun valueForHour(hour: Int): DayTimeValue {
            return values().first { hour > it.startTime };
        }
    }
}