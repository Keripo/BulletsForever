package com.bulletsforever.bullets;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * This is the app's preferences accessing Activity
 * This should be started from MenuHome
 * Multiple instances can be created/destroyed per app session, but only one can exist at a time
 */
public class MenuSettings extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		this.setTitle("Bullets Forever - Settings");
	}
}
