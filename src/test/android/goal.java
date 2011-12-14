package test.android;



import java.text.DecimalFormat;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class goal extends Activity {
	Button bt1;
	TextView textView1;//Penguin
	//TextView textView6;
	public String ngoal;//Penguin
	//public String ncurrrent;//Penguin
	//public static final String PREF_HEIGHT = "PREF_HEIGHT";
	//public static final String PREF_WEIGHT = "PREF_WEIGHT";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal);
        bt1=(Button)findViewById(R.id.button1);
        textView1=(TextView)findViewById(R.id.textView1);//Penguin
        showResults();////Penguin
        bt1.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        	Intent intent = new Intent();
        	intent.setClass(goal.this,TestFineActivity.class);
        	startActivity(intent);
        		
        	}
        	       	
        });
	}
	//Penguin
	public void showResults(){
		Log.e("test", "5");
		Bundle bundle = this.getIntent().getExtras();
		String goal=bundle.getString("KEY_Goal");
		Log.e("test", "6");
		//String currrent=bundle.getString("KEY_currrent");
		
		Log.e("test", "7");
		textView1.setText(goal);
		Log.e("test", "8");
		//textView6.setText(currrent);
		Log.e("test", "9");
		ngoal=bundle.getString("KEY_Goal");
		//ncurrrent=bundle.getString("KEY_currrent");
	}
	
}