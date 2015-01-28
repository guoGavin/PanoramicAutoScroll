package com.gavin.panoramicautoscroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/***
 * 自动滚动的横向的ScrollView，手动控制的时候自动停止自动控制，手势移开，自动继续滚动。
 * @author jwguo
 *
 */
public class AutoScrollHorizontalScrollView extends HorizontalScrollView {

	/** control scroll director **/
	private boolean derectionFlag = true;
	/** scroll end position**/
	private int autoScrollPosition = 0;
	/** add img count in child **/
	private int resetPositionCount = 0;
	/** control scroll **/
	private Handler mHandler = null;
	private Runnable ScrollRunnable = null;
	private Context mContext;
	/** content img **/
	private Bitmap showContent;
	/** child **/
	private LinearLayout childContainer;
	
	private ShowWay showWay = ShowWay.cycle;
	private Speed speed = Speed.slow; 
	private final static int slowLength = 1;
	private final static int mediumLength = 2;
	private final static int fastLength = 3;
	private int speedLength = slowLength;
	
	enum ShowWay{
		cycle,//Infinite scrolling
		repeat,//Scroll back and forth
	}
	
	enum Speed{
		slow, 
		medium,
		fast,
	}
	
	public AutoScrollHorizontalScrollView(Context context) {
		super(context);
		mContext = context;
		resetPositionCount = 0;
	}
	
	public AutoScrollHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		resetPositionCount = 0;
		getScrollXMax();
	}
	
	public AutoScrollHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		resetPositionCount = 0;
	}
	
	/** start method **/
	public void startAutoScroll(){
		mHandler = new Handler();
		ScrollRunnable= new Runnable() {
	         @Override
	         public void run() {
	    	 	 if(getScrollX() == 0){
	    	 		 if(showWay == ShowWay.repeat){
	    	 			derectionFlag = true;
	    	 		 }
	    	 	 }else if (getScrollX() == autoScrollPosition) {
	    	 		if(showWay == ShowWay.repeat){
	    	 			derectionFlag = false;
	    	 		 }else{
	    	 			//Add a picture behind
	 	    	 		addViewInChildContainer(true);
	    	 		 }
	             }
	    	 	 if(derectionFlag){
	    	 		scrollBy(speedLength, 0);
	    	 	 }else{
	    	 		scrollBy(-speedLength, 0);
	    	 	 }
	    	 	mHandler.postDelayed(this, 15);
	         }
	    };
		mHandler.post(ScrollRunnable);
	}
	
	/***
	 * pause scroll
	 */
	public void pauseAutoScroll(){
		mHandler.removeCallbacks(ScrollRunnable);
	}
	
	/***
	 * restart scroll
	 */
	public void restartAutoScroll(){
		mHandler.post(ScrollRunnable);
	}
	
	/**
	 * stop scroll
	 */
	public void stopAutoScroll(){
		mHandler.removeCallbacks(ScrollRunnable);
		mHandler = null;
		ScrollRunnable = null;
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		super.onWindowVisibilityChanged(visibility);
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		stopAutoScroll();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				pauseAutoScroll();
				break;
			case MotionEvent.ACTION_MOVE:
				if (getScrollX() == autoScrollPosition) {
					if(showWay == ShowWay.repeat){
						derectionFlag = false;
					}else{
						//Add a picture behind
		    	 		addViewInChildContainer(true);
					}
	             }else if(getScrollX() == 0){
	            	if(showWay == ShowWay.repeat){
						derectionFlag = true;
					}else{
						//Add a picture in front
						addViewInChildContainer(false);
					}
	             }
				break;
			case MotionEvent.ACTION_UP:
				restartAutoScroll();
				break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
    /***
	 * get scrollX() method can scroll max value
	 */
	private void getScrollXMax(){
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				if(resetPositionCount == 0){
					resetPositionCount++;
					autoScrollPosition = autoScrollPosition - getWidth();
				}
			}
		});
	}
	
	/**
	 * set cycle way
	 * @param speed
	 */
	public void setShowWay(ShowWay showWay) {
		this.showWay = showWay;
	}

	/**
	 * set scroll speed
	 * @param speed
	 */
	public void setSpeed(Speed speed) {
		this.speed = speed;
		switch (speed) {
			case slow:
				speedLength = slowLength;
				break;
			case fast:
				speedLength = fastLength;
				break;
			case medium:
				speedLength = mediumLength;
				break;
		}
	}

	public int getAutoScrollPosition() {
		return autoScrollPosition;
	}

	public void setContentAllWidth(int autoScrollPosition) {
		this.autoScrollPosition = autoScrollPosition;
	}

	public Bitmap getShowContent() {
		return showContent;
	}

	public void setShowContent(Bitmap showContent) {
		this.showContent = showContent;
		setContentAllWidth(showContent.getWidth()*2);
		childContainer = (LinearLayout)getChildAt(0);
		addViewInChildContainer(true);
	}
	
	/**
	 * add img in child
	 * @param derection 
	 * 			true : Add a picture behind
	 * 			else : Add a picture in front
	 */
	private void addViewInChildContainer(boolean derection){
		ImageView imageView = new ImageView(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		imageView.setLayoutParams(lp);
		imageView.setImageBitmap(showContent);
		Log.e("addViewInChildContainer", (Runtime.getRuntime()
				.totalMemory() / 1024 / 1024) + "MB"+getWidth());
		if(resetPositionCount == 0){
			ImageView imageView2 = new ImageView(mContext);
			imageView2.setLayoutParams(lp);
			imageView2.setImageBitmap(showContent);
			childContainer.addView(imageView);
			childContainer.addView(imageView2);
		}else{
			if(derection){
				childContainer.addView(imageView);
			}else{
				childContainer.addView(imageView,0);
				scrollTo(showContent.getWidth(), 0);
			}
			autoScrollPosition = autoScrollPosition+showContent.getWidth();
		}
	}
	
}
