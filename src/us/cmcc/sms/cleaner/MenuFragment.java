package us.cmcc.sms.cleaner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.internal.widget.IcsToast;
import me.sfce.library.Global;
import me.sfce.library.activity.BaseSlidingMenuActivity;

public class MenuFragment extends ListFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    IcsToast.makeText(getActivity(), "back", IcsToast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        MenuAdapter adapter = new MenuAdapter(getActivity());
        adapter.add(new MenuItem("白名单", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("黑名单", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("关键词", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("设置", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("我的基站", android.R.drawable.ic_menu_search));
//        adapter.add(new MenuItem("城市基站", android.R.drawable.ic_menu_search));
//        adapter.add(new MenuItem("可疑动态", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("版本升级", android.R.drawable.ic_menu_search));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(getActivity(), RuleListActivity.class);
                intent.putExtra("type", RuleAddActivity.TYPE_WHITE_NUMBER);
            	startActivity(intent);
                break;
            case 1:
                intent = new Intent(getActivity(), RuleListActivity.class);
                intent.putExtra("type", RuleAddActivity.TYPE_BLACK_NUMBER);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getActivity(), RuleListActivity.class);
                intent.putExtra("type", RuleAddActivity.TYPE_BLACK_WORD);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(getActivity(), LacScanActivity.class);
                startActivity(intent);
                break;
            case 7:
                intent = new Intent(getActivity(), CityLacActivity.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(getActivity(), SuspiciousNewsActivity.class);
                startActivity(intent);
                break;
            case 5:
                Global.Update.checkNewVersion(getActivity(), false);
                break;
        }
        getActivity().sendBroadcast(new Intent(BaseSlidingMenuActivity.ACTION_HIDE_MENU));
    }

    private class MenuItem {
        public String tag;
        public int iconRes;

        public MenuItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public class MenuAdapter extends ArrayAdapter<MenuItem> {

        public MenuAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
            icon.setImageResource(getItem(position).iconRes);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(getItem(position).tag);

            return convertView;
        }
    }
}
