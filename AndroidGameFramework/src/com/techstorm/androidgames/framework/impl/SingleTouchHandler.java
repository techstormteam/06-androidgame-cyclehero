package com.techstorm.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.techstorm.androidgames.framework.Pool;
import com.techstorm.androidgames.framework.PoolObjectFactory;
import com.techstorm.androidgames.framework.Input.TouchEvent;

public class SingleTouchHandler implements TouchHandler {
	boolean isTouch;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	public SingleTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {

			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			TouchEvent touchEvent = touchEventPool.newObject();
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouch = true;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouch = false;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouch = true;
				break;
			}
			touchEvent.x = this.touchX = (int) (event.getX() * scaleX);
			touchEvent.y = this.touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);
			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(pointer == 0) {
				return isTouch;
			} else {
				return false;
			}
		}
	}

	@Override
	public int getTouchX(int pointer) {
		// TODO Auto-generated method stub
		synchronized (this) {
			return touchX;
		}
	}

	@Override
	public int getTouchY(int pointer) {
		// TODO Auto-generated method stub
		synchronized (this) {
			return touchY;
		}
	}

	@Override
	public List<TouchEvent> getTouchEvent() {
		// TODO Auto-generated method stub
		synchronized (this) {
			int len = touchEvents.size();
			for(int i = 0; i < len; i++) {
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			
			return touchEvents;
		}
	}
}
