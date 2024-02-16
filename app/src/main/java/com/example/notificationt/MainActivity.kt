package com.example.notificationt

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    // declaring variables
    private lateinit var NotificationButton: Button

    private companion object {
        private const val CHANNEL_ID = "channel01"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        NotificationButton = findViewById(R.id.notifiButton);

        NotificationButton.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {

        val mainIntent = Intent(this, com.example.notificationt.Notification::class.java)

        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingIntent = PendingIntent.getActivity(this,1,mainIntent, PendingIntent.FLAG_IMMUTABLE)

        var notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New Notification")
            .setContentText("This is Notification Description")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        //cancel notification on click
        notificationBuilder.setAutoCancel(true)
        //add click intent
        notificationBuilder.setContentIntent(mainPendingIntent)

        //notification Manager
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1,notificationBuilder.build())
        }
    }
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NotificationChannel = NotificationChannel(CHANNEL_ID, "First Channel",
                NotificationManager.IMPORTANCE_HIGH)
            NotificationChannel.description ="Test Description for my channel"
            NotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC


            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(NotificationChannel)
        }
    }
}
