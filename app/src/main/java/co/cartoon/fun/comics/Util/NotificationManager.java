package co.cartoon.fun.comics.Util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.StringDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import co.cartoon.fun.comics.Activity.IntroActivity;
import co.cartoon.fun.comics.Activity.MyApp;
import co.cartoon.fun.comics.R;

/**
 * Created by TedPark on 2018. 2. 3..
 */

public class NotificationManager {

    private static final String GROUP_TED_PARK = "tedPark";
    public static Context context;

    @TargetApi(Build.VERSION_CODES.O)
    public static void createChannel(Context context) {
        NotificationManager.context = context;
        NotificationChannelGroup group1 = new NotificationChannelGroup(GROUP_TED_PARK, GROUP_TED_PARK);
        getManager(context).createNotificationChannelGroup(group1);
    }

    private static android.app.NotificationManager getManager(Context context) {
        return (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void deleteChannel(Context context, @Channel String channel) {
        getManager(context).deleteNotificationChannel(channel);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void sendNotification(int id, @Channel String channel, String title, String message) {
        NotificationChannel channelMessage = new NotificationChannel(Channel.MESSAGE,
                title, android.app.NotificationManager.IMPORTANCE_DEFAULT);
        channelMessage.setDescription(message);
        channelMessage.setGroup(GROUP_TED_PARK);
        channelMessage.setLightColor(Color.GREEN);
        channelMessage.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(NotificationManager.context).createNotificationChannel(channelMessage);

        NotificationChannel channelComment = new NotificationChannel(Channel.COMMENT,
                title, android.app.NotificationManager.IMPORTANCE_DEFAULT);
        channelComment.setDescription(message);
        channelComment.setGroup(GROUP_TED_PARK);
        channelComment.setLightColor(Color.BLUE);
        channelComment.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager(NotificationManager.context).createNotificationChannel(channelComment);

        NotificationChannel channelNotice = new NotificationChannel(Channel.NOTICE,
                title, android.app.NotificationManager.IMPORTANCE_HIGH);
        channelNotice.setDescription(message);
        channelNotice.setGroup(GROUP_TED_PARK);
        channelNotice.setLightColor(Color.RED);
        channelNotice.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager(NotificationManager.context).createNotificationChannel(channelNotice);


        Intent intent = new Intent(NotificationManager.context, IntroActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationManager.context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(NotificationManager.context, channel)
                .setLargeIcon(BitmapFactory.decodeResource(NotificationManager.context.getResources(),R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true)
                .setNumber(1);

        Bitmap bitmap = BitmapFactory.decodeResource(NotificationManager.context.getResources(),R.drawable.live_notification);
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);
        bigPictureStyle.bigPicture(bitmap); // 아래로 밀면 보여질 그림
        builder.setStyle(bigPictureStyle);
        getManager(NotificationManager.context).notify(id, builder.build());
    }

    private static int getSmallIcon() {
        return R.mipmap.ic_launcher;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Channel.MESSAGE,
            Channel.COMMENT,
            Channel.NOTICE
    })
    public @interface Channel {
        String MESSAGE = "message";
        String COMMENT = "comment";
        String NOTICE = "notice";
    }

}
