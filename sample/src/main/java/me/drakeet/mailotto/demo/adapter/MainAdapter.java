package me.drakeet.mailotto.demo.adapter;

import android.widget.TextView;
import com.camnter.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.camnter.easyrecyclerview.holder.EasyRecyclerViewHolder;
import me.drakeet.mailotto.demo.R;

/**
 * Description：MainAdapter
 * Created by：CaMnter
 * Time：2016-04-19 13:21
 */
public class MainAdapter extends EasyRecyclerViewAdapter {

    @Override public int[] getItemLayouts() {
        return new int[] { R.layout.item_main };
    }


    @Override public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, int position) {
        Class c = (Class) this.getList().get(position);
        if (c == null) return;
        TextView textView = viewHolder.findViewById(R.id.main_item_tv);
        textView.setText(c.getSimpleName());
    }


    @Override public int getRecycleViewItemType(int position) {
        return 0;
    }
}
