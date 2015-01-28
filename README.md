#PanoramicAutoScroll
一个全景360自动（手动）循环滚动的演示demo
##效果图
![Screenshot](https://github.com/guoGavin/PanoramicAutoScroll/blob/master/demo_show.gif)
##功能
* 按照一定的速度自动滚动。</br>
* 当手指进行操作则停止滚动，手指放开则继续滚动。</br>
* 可以设置为无限循环滚动或者无限往复滚动。</br>
* 可以设置滚动速度。</br>

##Layout
```xml
<com.gavin.panoramicautoscroll.AutoScrollHorizontalScrollView
    android:id="@+id/scrollView"
	  android:layout_width="fill_parent"
	  android:layout_height="wrap_content"
	  android:scrollbars="none"
	  android:background="@android:color/transparent" >
	  
	  <LinearLayout 
	      android:layout_width="wrap_content"
	      android:layout_height="wrap_content"
	      android:orientation="horizontal" />
        
    </com.gavin.panoramicautoscroll.AutoScrollHorizontalScrollView>
```
##滚动方式
```java
enum ShowWay{
		cycle,//Infinite scrolling
		repeat,//Scroll back and forth
	}
```
##自动滚动速度
```java
enum Speed{
		slow, 
		medium,
		fast,
	}
```
##设置以及启动
```java
autoScrollView.setShowContent(bitmap);
autoScrollView.setSpeed(speedResult);
autoScrollView.setShowWay(showWayResult);
autoScrollView.startAutoScroll();
```
