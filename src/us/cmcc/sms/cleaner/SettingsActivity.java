package us.cmcc.sms.cleaner;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-9
 * Time: 下午11:38
 */
public class SettingsActivity extends SherlockPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);

    }
}
