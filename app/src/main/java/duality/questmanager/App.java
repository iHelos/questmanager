package duality.questmanager;

import android.app.Application;
import com.bettervectordrawable.VectorDrawableCompat;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        VectorDrawableCompat.enableResourceInterceptionFor(getResources(),
                R.drawable.check_circle_outline,
                R.drawable.checkbox_blank_circle_outline__1_,
                R.drawable.close_circle_outline,
                R.drawable.coin,
                R.drawable.inbox,
                R.drawable.information,
                R.drawable.logout_variant,
                R.drawable.menu,
                R.drawable.outbox,
                R.drawable.pencil,
                R.drawable.plus,
                R.drawable.settings,
                R.drawable.login_logo
                );

        // вызов VectorDrawableCompat.enableResourceInterceptionFor()
    }
}