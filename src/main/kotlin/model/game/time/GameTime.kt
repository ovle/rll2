package model.game.time;

class GameTime(var day:Long, var hour:Long, var minute:Long) {

    val GAME_LOOP_DELAY = 250
    val NANOSECS_IN_MILLISEC = 1000000

    //public static final int MILLIS_IN_GAME_MINUTE = 1000;
    val MILLIS_IN_GAME_MINUTE = 20
    val MILLIS_IN_GAME_HOUR = MILLIS_IN_GAME_MINUTE * 60
    val MILLIS_IN_GAME_DAY = MILLIS_IN_GAME_HOUR * 24

    private var gameTime: Long = 0

    fun reset() {
        gameTime = 0
    }

    fun update(diff: Long) {
        gameTime += diff / NANOSECS_IN_MILLISEC

        this.updateTimeValues()
    }

    override fun toString() = "day $day $hour:$minute"

    private fun updateTimeValues() {
        day = gameTime / MILLIS_IN_GAME_DAY
        var rest = gameTime % MILLIS_IN_GAME_DAY
        hour = rest / MILLIS_IN_GAME_HOUR
        rest %= MILLIS_IN_GAME_HOUR
        minute = rest / MILLIS_IN_GAME_MINUTE
    }
}

