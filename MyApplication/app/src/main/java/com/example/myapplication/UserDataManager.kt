import android.content.Context
import android.content.SharedPreferences

class UserDataManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "UserData"
        private const val KEY_JUMP_COUNT = "jumpCount"
        private const val KEY_LAST_TIMESTAMP = "lastTimestamp"
        private const val SEVEN_DAYS_IN_MILLIS = 7 * 24 * 60 * 60 * 1000 // 7 days in milliseconds
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveJumpCount(jumpCount: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_JUMP_COUNT, jumpCount)
        editor.putLong(KEY_LAST_TIMESTAMP, System.currentTimeMillis())
        editor.apply()
    }

    fun getJumpCount(): Int {
        return sharedPreferences.getInt(KEY_JUMP_COUNT, 0)
    }

    fun isJumpHistoryExpired(): Boolean {
        val lastTimestamp = sharedPreferences.getLong(KEY_LAST_TIMESTAMP, 0)
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastTimestamp) >= SEVEN_DAYS_IN_MILLIS
    }

    fun clearJumpHistory() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_JUMP_COUNT)
        editor.remove(KEY_LAST_TIMESTAMP)
        editor.apply()
    }
}
