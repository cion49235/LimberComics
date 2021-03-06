package co.cartoon.fun.comics.View;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;

import co.cartoon.fun.comics.Activity.helper_main;

public class NinjaContextWrapper extends ContextWrapper {
    private final Context context;

    public NinjaContextWrapper(Context context) {
        super(context);
        this.context = context;
        helper_main.setTheme(context);
    }

    @Override
    public Resources.Theme getTheme() {
        return context.getTheme();
    }
}
