package com.tudny.wkdapp.gestures;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.Objects;

public class WKDGestureListener extends GestureDetector.SimpleOnGestureListener {
	private static final String DEBUG_TAG = WKDGestureListener.class.getSimpleName();

	private static final Float SWIPE_EPSILON = 20.0f;
	private static final Float SWIPE_VELOCITY_EPSILON = 30.0f;

	private final OnUpSwipeListener onUpSwipeListener;
	private final OnDownSwipeListener onDownSwipeListener;
	private final OnLeftSwipeListener onLeftSwipeListener;
	private final OnRightSwipeListener onRightSwipeListener;

	public WKDGestureListener(OnUpSwipeListener onUpSwipeListener, OnDownSwipeListener onDownSwipeListener, OnLeftSwipeListener onLeftSwipeListener, OnRightSwipeListener onRightSwipeListener) {
		this.onUpSwipeListener = onUpSwipeListener;
		this.onDownSwipeListener = onDownSwipeListener;
		this.onLeftSwipeListener = onLeftSwipeListener;
		this.onRightSwipeListener = onRightSwipeListener;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		boolean result = false;

		float diffX = event2.getX() - event1.getX();
		float diffY = event2.getY() - event1.getY();

		try {

			if (Math.abs(diffX) > Math.abs(diffY)) {
				if (Math.abs(diffX) > SWIPE_EPSILON
						&& Math.abs(velocityX) > SWIPE_VELOCITY_EPSILON) {
					if (diffX > 0) {
						Log.d(DEBUG_TAG, "right_swipe");
						Objects.requireNonNull(onRightSwipeListener).onRightSwipe();
					} else {
						Log.d(DEBUG_TAG, "left_swipe");
						Objects.requireNonNull(onLeftSwipeListener).onLeftSwipe();
					}
					result = true;
				}
			} else if (Math.abs(diffY) > SWIPE_EPSILON
					&& Math.abs(velocityY) > SWIPE_VELOCITY_EPSILON) {
				if (diffY > 0) {
					Log.d(DEBUG_TAG, "down_swipe");
					Objects.requireNonNull(onDownSwipeListener).onDownSwipe();
				} else {
					Log.d(DEBUG_TAG, "up_swipe");
					Objects.requireNonNull(onUpSwipeListener).onUpSwipe();
				}

				result = true;
			}

		} catch (Exception e){
			Log.e(DEBUG_TAG, "No listener was found!");
			result = false;
		}

		return result;
	}
}
