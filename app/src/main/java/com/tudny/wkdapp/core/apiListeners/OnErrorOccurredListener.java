package com.tudny.wkdapp.core.apiListeners;

import com.android.volley.VolleyError;

public interface OnErrorOccurredListener {
	void onErrorOccurred(VolleyError error);
}
