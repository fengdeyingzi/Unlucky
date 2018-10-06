package com.unlucky.main;

import android.os.Bundle;
import android.widget.Toast;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.unlucky.main.Unlucky;
import com.xl.game.tool.Log;

/**
 * Android version access
 */
public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        //config.useAccelerometer = false;
        //config.useCompass = false;
        config.useGLSurfaceView20API18 = true;
		Toast.makeText(this,"gdx version:"+ Version.VERSION,0).show();
		initialize(new Unlucky(), config);
	}
	public void debug(java.lang.String tag, java.lang.String message)
	{
		Log.e(tag,message);

	}

	public void debug(java.lang.String tag, java.lang.String message, java.lang.Throwable exception) 
	{
		Log.e(tag,message);
	}

	public void log(java.lang.String tag, java.lang.String message) 
	{
		Log.e(tag,message);
	}
/*
	public void log(java.lang.String tag, java.lang.String message, java.lang.Throwable exception) 
	{
		super.log(tag,message,exception);
	}

	public void error(java.lang.String tag, java.lang.String message) 
	{
		Log.e(tag,message);
	}

	public void error(java.lang.String tag, java.lang.String message, java.lang.Throwable exception)
	{
		super.error(tag,message,exception);
	}
	*/
}
