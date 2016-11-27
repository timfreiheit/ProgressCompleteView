package de.timfreiheit.progress;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

class Utils {

    static float dpToPx(Context context, int dp){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    static int getThemeAccentColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

}

