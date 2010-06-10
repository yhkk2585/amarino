package at.abraxas.amarino.plugins.batterylevel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;
import at.abraxas.amarino.AmarinoIntent;
import at.abraxas.amarino.plugins.AbstractPluginService;

public class BackgroundService extends AbstractPluginService {
	
	private static final String TAG = "BatteryLevel Plugin";
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			
			Intent i = new Intent(AmarinoIntent.ACTION_SEND);
			i.putExtra(AmarinoIntent.EXTRA_PLUGIN_ID, pluginId);
			i.putExtra(AmarinoIntent.EXTRA_DATA_TYPE, AmarinoIntent.INT_EXTRA);
			i.putExtra(AmarinoIntent.EXTRA_DATA, batteryLevel);
			
			if (DEBUG) Log.d(TAG, "send: " + batteryLevel);
			sendBroadcast(i);
		}
	};
	

	@Override
	public void init() {
		if (!pluginEnabled){
			/* here should be your specific initialization code */
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			pluginId = prefs.getInt(EditActivity.KEY_PLUGIN_ID, -1);
			
			IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(receiver, filter);
			
			// don't forget to set plug-in enabled if everything was initialized fine
			pluginEnabled = true;
		}
	}


	@Override
	public void onDestroy() {
		if (pluginEnabled){
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}


	@Override
	public String getTAG() {
		return TAG;
	}


}
