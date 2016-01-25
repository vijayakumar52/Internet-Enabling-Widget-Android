package com.example.internetenablerwidget;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.adsonik.dataenablerwidget.R;

public class MyWidgetIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 0;
	ConnectivityManager conman;
	   NetworkInfo mobile;
	   RemoteViews remoteViews; 
	   

	@Override
	public void onReceive(Context context, Intent intent) {
		 conman= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 mobile=conman.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		 remoteViews= new RemoteViews(context.getPackageName(), R.layout.activity_main);
		if(intent.getAction().equals("pl.looksok.intent.action.CHANGE_PICTURE")){
			updateWidgetPictureAndButtonListener(context);
		}else if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
			if(!mobile.isConnected()){
				 remoteViews.setImageViewResource(R.id.widget_image, R.drawable.disabled);
				 
			}else if(mobile.isConnected()){
				 remoteViews.setImageViewResource(R.id.widget_image, R.drawable.enabled);
			}
			MainActivity.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		
			
		 if(!mobile.isConnected()){
			 setMobileDataEnabled(context,true);
			 Toast.makeText(context, "Mobile Data is Enabled", Toast.LENGTH_SHORT).show();
			 remoteViews.setImageViewResource(R.id.widget_image, R.drawable.enabled);
		 }
			 
		 else{
			 setMobileDataEnabled(context,false);
			 Toast.makeText(context, "Mobile Data is Disabled", Toast.LENGTH_SHORT).show();
			 remoteViews.setImageViewResource(R.id.widget_image, R.drawable.disabled);
		 }
		
		

		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.widget_image, MainActivity.buildButtonPendingIntent(context));

		MainActivity.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

	private int getImageToSet() {
		clickCount++;
		return clickCount % 2 == 0 ? R.drawable.disabled : R.drawable.enabled;
	}
	private void setMobileDataEnabled(Context context, boolean enabled) {
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    Class conmanClass = null;
		try {
			conmanClass = Class.forName(conman.getClass().getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Field iConnectivityManagerField = null;
		try {
			iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    iConnectivityManagerField.setAccessible(true);
	    Object iConnectivityManager = null;
		try {
			iConnectivityManager = iConnectivityManagerField.get(conman);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Class iConnectivityManagerClass = null;
		try {
			iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Method setMobileDataEnabledMethod = null;
		try {
			setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    setMobileDataEnabledMethod.setAccessible(true);

	    try {
			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}