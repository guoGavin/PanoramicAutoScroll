package com.gavin.panoramicautoscroll;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gavin.panoramicautoscroll.AutoScrollHorizontalScrollView.ShowWay;
import com.gavin.panoramicautoscroll.AutoScrollHorizontalScrollView.Speed;
import com.gavin.panoramicautoscroll.GestureUtils.Screen;

public class MainActivity extends Activity {

	private Button showBtn;
	private RadioGroup showWay,speed;
	private ShowWay showWayResult = ShowWay.cycle;
	private Speed speedResult = Speed.slow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showBtn = (Button) findViewById(R.id.show);
		showWay = (RadioGroup) findViewById(R.id.ShowWay);
		speed = (RadioGroup) findViewById(R.id.Speed);
		showWay.setOnCheckedChangeListener(checkedChange);
		speed.setOnCheckedChangeListener(checkedChange);
		showBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTouristDialog();
			}
		});
	}
	
	RadioGroup.OnCheckedChangeListener  checkedChange = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch(checkedId){
				case R.id.cycle:
					showWayResult = ShowWay.cycle;
					break;
				case R.id.repeat:
					showWayResult = ShowWay.repeat;
					break;
				case R.id.slow:
					speedResult = Speed.slow;
					break;
				case R.id.medium:
					speedResult = Speed.medium;
					break;
				case R.id.fast:
					speedResult = Speed.fast;
					break;
			}
		}
	};

	PopupWindow popW = null;
	private void showTouristDialog() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		Screen screen = GestureUtils.getScreenPix(getApplicationContext());
		View imgEntryView = inflater.inflate(R.layout.pop_window, null);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		/**set params ,begin**/
		AutoScrollHorizontalScrollView scrollView = (AutoScrollHorizontalScrollView) imgEntryView
				.findViewById(R.id.scrollView);
		scrollView.setShowContent(bitmap);
		scrollView.setSpeed(speedResult);
		scrollView.setShowWay(showWayResult);
		scrollView.startAutoScroll();
		System.out.println("showWay:"+showWayResult.toString()+";speed:"+speedResult.toString());
		/**set params ,end**/
		popW = new PopupWindow(getApplicationContext());
		popW.setContentView(imgEntryView);
		popW.setWidth(screen.widthPixels);
		popW.setHeight(bitmap.getHeight()+105);
		popW.setBackgroundDrawable(new BitmapDrawable());
		popW.setFocusable(false);
		popW.setTouchable(true);
		popW.setOutsideTouchable(true);
		popW.showAtLocation(((ViewGroup)findViewById(android.R.id.content)).getChildAt(0), Gravity.LEFT | Gravity.TOP, 0, screen.heightPixels / 4);
	}
}
