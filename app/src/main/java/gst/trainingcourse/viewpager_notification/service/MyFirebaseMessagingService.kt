package gst.trainingcourse.viewpager_notification.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import gst.trainingcourse.viewpager_notification.MainActivity
import gst.trainingcourse.viewpager_notification.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "Refreshed token: $token");
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        var screenId = 0
        if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.data["screenId"]?.toInt() ?: 0
        }

        if (remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification!!.body, screenId)
        }

    }

    private fun sendNotification(body: String?, screenId: Int) {

        val intent = Intent(this, MainActivity::class.java)
//If set, and the activity being launched is already running in the current task,
// then instead of launching a new instance of that activity, all of the other activities
        // on top of it will be closed and this Intent will be delivered to the (now on top)
        // old activity as a new Intent.
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("screenId", screenId)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT
/*Flag indicating that this PendingIntent can be used only once.*/
        )
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, "Notification")
            .setSmallIcon(R.drawable.ic_plane_icon)
            .setContentTitle("Push Notification FCM")
            .setContentText(body).setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)
        val notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())

    }





}