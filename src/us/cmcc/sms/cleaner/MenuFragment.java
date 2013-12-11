package us.cmcc.sms.cleaner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends ListFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MenuAdapter adapter = new MenuAdapter(getActivity());
        adapter.add(new MenuItem("白名单", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("黑名单", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("设置", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("基站扫描", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("城市基站", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("可疑动态", android.R.drawable.ic_menu_search));
        adapter.add(new MenuItem("假基站追踪", android.R.drawable.ic_menu_search));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
            case 0:
                break;
            case 1:
                startActivity(new Intent(getActivity(), BlackActivity.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), LacScanActivity.class));
                break;
            case 4:
                startActivity(new Intent(getActivity(), CityLacActivity.class));
                break;
            case 5:
                startActivity(new Intent(getActivity(), SuspiciousNewsActivity.class));
                break;
            case 6:
                startActivity(new Intent(getActivity(), FakeTrackActivity.class));
                break;
        }
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
