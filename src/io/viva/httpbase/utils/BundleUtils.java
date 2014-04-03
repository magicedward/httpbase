package io.viva.httpbase.utils;

import java.util.Iterator;
import java.util.Set;

import android.os.Bundle;
import android.util.Log;

public class BundleUtils {
	public static void print(Bundle paramBundle, String paramString) {
		if (paramBundle == null) {
			return;
		}
		Set<String> localSet = paramBundle.keySet();
		Iterator<String> localIterator = localSet.iterator();
		while (localIterator.hasNext()) {
			String str = localIterator.next();
			if (str != null) {
				Log.i(paramString, "BundleUtils -- print -- key:" + str + ",value:" + paramBundle.get(str).toString());
			}
		}
	}
}
