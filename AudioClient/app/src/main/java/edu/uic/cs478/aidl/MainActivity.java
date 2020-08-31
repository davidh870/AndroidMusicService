package edu.uic.cs478.aidl;

import android.app.Activity ;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.uic.cs478.aidl.MusicPlayerService;

public class MainActivity extends Activity {
	// All buttons and textview in layout
	private Button startServiceButton;
	private Button startSongButton;
	private Button playSongButton;
	private Button pauseSongButton;
	private Button stopSongButton;
	private Button stopServiceButton;
	private Spinner spinner;
	private TextView messageTextview;

	static String songID; // Keeps track of song

	protected static final String TAG = "BoundService";
	static MusicPlayerService mMusicPlayerService;
	static boolean mIsBound = false;

	// Intent for start service
	public Intent i = null;

	// Receiver that keeps track of when song is complete
	private MyReceiver mReceiver;
	private IntentFilter mFilter;


	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);

		setContentView(R.layout.activity_main);

		// Load Music Player Service intent
		i = new Intent(MusicPlayerService.class.getName());
		ResolveInfo info = getPackageManager().resolveService(i, 0);
		i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

		// Register Receiver
		mReceiver = new MyReceiver();
		mFilter = new IntentFilter("musicFilter");
		registerReceiver(mReceiver, mFilter);

		// Load all buttons and textview
		startServiceButton = (Button) findViewById(R.id.startServiceButtonID);
		startSongButton = (Button) findViewById(R.id.startSongButtonID);
		playSongButton = (Button) findViewById(R.id.playButtonID);
		pauseSongButton = (Button) findViewById(R.id.pauseButtonID);
		stopSongButton = (Button) findViewById(R.id.stopButtonID);
		stopServiceButton = (Button) findViewById(R.id.stopServiceButtonID);
		messageTextview = (TextView) findViewById(R.id.textviewID) ;

		// Load spinner and populate list with song id
		spinner = (Spinner) findViewById(R.id.spinnerID);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.songList));

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setEnabled(false);
		spinner.setAdapter(adapter);
		spinner.setSelection(0,false);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				if(!mIsBound){
					startBindService();
				}

				Log.i(TAG, "SONG SELECTED " + (position + 1));
				messageTextview.setText("Song Selected: " + (position + 1));

				startSongButton.setEnabled(true);
				MainActivity.songID = String.valueOf(position + 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		// Start service when start service button is pressed
		startServiceButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				startForegroundService(i);
				startServiceButton.setEnabled(false);
				spinner.setEnabled(true);
				stopServiceButton.setEnabled(true);
			}
		});


		// Start specific song when start song button is clicked
		startSongButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				try{
					if(mIsBound){
						if(mMusicPlayerService != null){
							mMusicPlayerService.startSong(MainActivity.songID);

							// Change button configs
							spinner.setEnabled(false);
							startSongButton.setEnabled(false);
							pauseSongButton.setEnabled(true);
							stopSongButton.setEnabled(true);
							stopServiceButton.setEnabled(false);
						}

					}
				} catch (RemoteException e) {
					Log.e(TAG, e.toString());
				}
			}
		});

		// Resumes song when paused
		playSongButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try{
					if(mIsBound){
						if(mMusicPlayerService != null){
							// Change button configs
							playSongButton.setEnabled(false);
							pauseSongButton.setEnabled(true);

							mMusicPlayerService.playSong();
						}
					}

				} catch (RemoteException e) {
					Log.e(TAG, e.toString());
				}
			}
		});

		// Pauses song when playing
		pauseSongButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try{
					if(mIsBound){
						if(mMusicPlayerService != null){
							// Change button configs
							playSongButton.setEnabled(true);
							pauseSongButton.setEnabled(false);

							mMusicPlayerService.pauseSong();
						}
					}

				} catch (RemoteException e) {
					Log.e(TAG, e.toString());
				}
			}
		});

		// stop song when playing or not and unbind
		stopSongButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try{
					if(mIsBound){
						if(mMusicPlayerService != null){
							// Change button configs
							playSongButton.setEnabled(false);
							pauseSongButton.setEnabled(false);
							stopSongButton.setEnabled(false);
							stopServiceButton.setEnabled(true);

							spinner.setEnabled(true);
							messageTextview.setText("");

							mMusicPlayerService.stopSong(); // stop song

							mIsBound = false; // Reset to false
							unbindService(mConnection); // Unbind service
						}
					}

				} catch (RemoteException e) {
					Log.e(TAG, e.toString());
				}
			}
		});

		// Stop service when stop service button is clicked
		stopServiceButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				// Change button configs
				startServiceButton.setEnabled(true);
				stopServiceButton.setEnabled(false);
				startSongButton.setEnabled(false);
				playSongButton.setEnabled(false);
				pauseSongButton.setEnabled(false);
				spinner.setEnabled(false);
				messageTextview.setText("");

				stopService(i); // Stop service when destroyed
			}
		});

	}


	// Start the bind service
	public void startBindService(){
		// Bind Service
		if(!mIsBound){
			boolean b = false;
			b = bindService(i, MainActivity.mConnection, Context.BIND_AUTO_CREATE);

			// If successful connection play song
			if (b) {
				//mMusicPlayerService.startSong("1");
				Log.i(TAG, "BindService() succeeded!");
			} else {
				Log.i(TAG, "BindService() failed!");
			}
		}
	}

	static final ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder iservice) {
			Log.i(TAG, "SERVICE CONNECTED");
			MainActivity.mMusicPlayerService = MusicPlayerService.Stub.asInterface(iservice);
			MainActivity.mIsBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			Log.i(TAG, "SERVICE DISCONNECTED");
			mMusicPlayerService = null;
			mIsBound = false;
		}
	};


	// Receiver will keep track of when song is complete, unbind service and change button settings
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null){
				if(!intent.getStringExtra("song").isEmpty()){
					if(mIsBound){
						// Change button configs
						playSongButton.setEnabled(false);
						pauseSongButton.setEnabled(false);
						stopSongButton.setEnabled(false);
						stopServiceButton.setEnabled(true);

						spinner.setEnabled(true);
						messageTextview.setText("");

						unbindService(mConnection);
						mIsBound = false;
					}
				}
			}
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		stopService(i); // Stop service when destroyed

		// Unbind Service
		if (mIsBound) {
			unbindService(mConnection);
		}
	}


}
