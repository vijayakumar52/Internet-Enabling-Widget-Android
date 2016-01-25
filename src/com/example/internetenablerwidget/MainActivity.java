package com.example.internetenablerwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.adsonik.dataenablerwidget.R;

public class MainActivity extends AppWidgetProvider {

	ConnectivityManager conman;
	   NetworkInfo mobile;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
	
		 conman= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 mobile=conman.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		 RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main);
		 if(!mobile.isConnected()){
			 remoteViews.setImageViewResource(R.id.widget_image, R.drawable.disabled);
		 }else if(mobile.isConnected()){
			 remoteViews.setImageViewResource(R.id.widget_image, R.drawable.enabled);
		 }
		
		remoteViews.setOnClickPendingIntent(R.id.widget_image, buildButtonPendingIntent(context));
		pushWidgetUpdate(context, remoteViews);
	}

	public static PendingIntent buildButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("pl.looksok.intent.action.CHANGE_PICTURE");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, MainActivity.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "Bye Bye!!!!", Toast.LENGTH_SHORT).show();
	}
	
}