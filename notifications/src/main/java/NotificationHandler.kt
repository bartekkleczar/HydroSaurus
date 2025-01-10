import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.notifications.R

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"

    fun showNotification(){
        Log.d("Notification", "runs")
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Test")
            .setContentText("Test")
            .setSmallIcon(R.drawable.waterdrop)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }
}