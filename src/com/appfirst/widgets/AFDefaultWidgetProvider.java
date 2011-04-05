/*
 * Copyright 2009-2011 AppFirst, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appfirst.widgets;

import java.util.Date;

import com.appfirst.communication.Helper;
import com.appfirst.monitoring.MainApplication;
import com.appfirst.monitoring.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * @author Bin Liu
 * 
 */
public class AFDefaultWidgetProvider extends AppWidgetProvider {
	public class WidgetUpdateActivity extends Activity {

		public WidgetUpdateActivity() {
			super();
		}

	}

	@Override
	public void onReceive(Context context, Intent intent)

	{
		super.onReceive(context, intent);

		if (intent.getAction().equals("com.google.app.myapp.CLICK")) {
			// Write here your button click code
			Toast.makeText(context, "It works!!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onEnabled(Context context) {
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// Loop through all widgets to display an update
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			// attach onclick() intent to the button.
			Intent clickintent = new Intent(context, UpdateService.class);
			PendingIntent pendingIntentClick = PendingIntent.getService(
					context, 0, clickintent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.widget_refresh_button,
					pendingIntentClick);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		context.startService(new Intent(context, UpdateService.class));

	}

	/**
	 * Set the server count in the widget.
	 * 
	 * @param views
	 *            the remote view instance
	 * @param context
	 *            the application running context.
	 */
	protected static void setServerCount(RemoteViews views, Context context) {
		MainApplication.loadServerList(Helper.getServerListUrl(context));
		String serverCount = String
				.format("%d", MainApplication.servers.size());
		views.setTextViewText(R.id.widget_server_count, serverCount);
	}

	/**
	 * Set the alert count in the widget.
	 * 
	 * @param views
	 *            the remote view instance
	 * @param context
	 *            the application running context.
	 */
	protected static void setAlertCount(RemoteViews views, Context context) {
		MainApplication.loadAlertList(Helper.getAlertUrl(context, -1));
		String alertCount = String.format("%d", MainApplication.getAlerts().size());
		views.setTextViewText(R.id.widget_alert_count, alertCount);
	}

	/**
	 * 
	 * @param context
	 * @param manager
	 * @param thisWidget
	 */
	protected static void setLoading(Context context, AppWidgetManager manager,
			ComponentName thisWidget) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		views.setTextViewText(R.id.widget_text, "Updating...");
		views.setViewVisibility(R.id.widget_refresh_button, View.INVISIBLE);
		manager.updateAppWidget(thisWidget, views);
	}

	/**
	 * Set the updated time in the widget.
	 * 
	 * @param views
	 *            the remote view instance
	 * @param context
	 *            the application running context.
	 */
	protected static void setUpdateTime(RemoteViews views, Context context) {
		String text = Helper.formatTime(System.currentTimeMillis());
		views.setTextViewText(R.id.widget_text, text);
		
	}

	public static class UpdateService extends Service {
		@Override
		public void onStart(Intent intent, int startId) {
			Log.d("AFDefaultWidgetProvider.UpdateService", "onStart()");

			// Push update for this widget to the home screen
			ComponentName thisWidget = new ComponentName(this,
					AFDefaultWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			setLoading(this, manager, thisWidget);
			RemoteViews updateViews = buildUpdate(this);
			manager.updateAppWidget(thisWidget, updateViews);
			Log.d("AFDefaultWidgetProvider.UpdateService", "Update done. ");
		}

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		/**
		 * Update the view context inside the service. All the update function
		 * should be called here.
		 * 
		 * @param context
		 *            the running context of the application.
		 * @return the udpated views
		 */
		public RemoteViews buildUpdate(Context context) {
			// Pick out month names from resources
			RemoteViews views = null;
			views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			setServerCount(views, context);
			setAlertCount(views, context);
			setUpdateTime(views, context);
			views.setViewVisibility(R.id.widget_refresh_button, View.VISIBLE);
			return views;
		}
	}
}