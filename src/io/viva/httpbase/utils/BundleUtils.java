package io.viva.httpbase.utils;

import java.util.Iterator;

import android.os.Bundle;
import android.util.Log;

public class BundleUtils {

	public static void print(Bundle bundle, String tag) {
		if (bundle == null) {
			return;
		}
		Iterator<String> it = bundle.keySet().iterator();
		while (it.hasNext()) {
			String str = it.next();
			if (str != null) {
				Log.i(tag, "BundleUtils -- print -- key:" + str + ",value:" + bundle.get(str).toString());
			}
		}
	}
}
