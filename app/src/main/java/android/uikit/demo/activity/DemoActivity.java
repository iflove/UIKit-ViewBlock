package android.uikit.demo.activity;

import android.content.Context;
import android.content.UIKitIntent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.uikit.ViewBlock;
import android.uikit.demo.R;
import android.uikit.demo.utils.ResUtil;
import android.uikit.demo.viewblock.ContentStartViewBlock;
import android.uikit.demo.viewblock.ManagerBlock;
import android.uikit.demo.viewblock.TabBlock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class DemoActivity extends android.uikit.UIKitActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsUsedAndroidLayout(true);
        setContentView(new ListViewBlock(this));
    }

    private class ListViewBlock extends ViewBlock implements AdapterView.OnItemClickListener {
        ListView listView;

        ListViewBlock(@NonNull Context context) {
            super(context, android.R.layout.list_content);
        }

        @Override
        public void onCreateView() {
            super.onCreateView();
            listView = bindViewById(android.R.id.list);
            List<String> listDataString = ResUtil.getListString(R.array.list);
            listView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listDataString));
            listView.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startActivity(GeneralActivity.class);
                    break;
                case 1:
                    startActivity(UIKitActivity.class);
                    break;
                case 2:
                    ContentStartViewBlock viewBlock = new ContentStartViewBlock(this.getContext(), ((ViewGroup) getBlockingView()));
                    UIKitIntent uiKitIntent = new UIKitIntent(this, viewBlock);
                    viewBlock.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    startViewBlock(uiKitIntent);
                    break;
                case 3:
                    setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out);
                    TabBlock tabBlock = new TabBlock(getContext());
                    uiKitIntent = new UIKitIntent(this, tabBlock);
                    startViewBlock(uiKitIntent);
                    break;
                case 4:
                    ManagerBlock managerBlock = new ManagerBlock(getContext());
                    managerBlock.setCustomAnimations(0, 0);
                    setCustomAnimations(0, 0);
                    uiKitIntent = new UIKitIntent(this, managerBlock);
                    startViewBlock(uiKitIntent);
                    break;
                default:
                    break;
            }
        }
    }

}
