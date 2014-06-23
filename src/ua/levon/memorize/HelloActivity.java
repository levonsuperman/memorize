package ua.levon.memorize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class HelloActivity extends Activity {
	
	TextView memorize;
	TextView copyright;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello);
		
		memorize=(TextView)findViewById(R.id.memorize);
		Typeface tf=Typeface.createFromAsset(getAssets(), "fonts/Comfortaa-Regular.ttf");
		memorize.setTypeface(tf);
		
		copyright=(TextView)findViewById(R.id.copyright);
		
		overridePendingTransition(R.animator.s1, R.animator.s2);
		
	}
	
	public void onClick (View v) {
		
		Intent toMain=new Intent(HelloActivity.this, MainActivity.class);
		HelloActivity.this.startActivity(toMain);
		
	}
	
}
