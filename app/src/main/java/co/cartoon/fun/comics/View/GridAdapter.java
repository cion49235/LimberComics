package co.cartoon.fun.comics.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

import co.cartoon.fun.comics.Unit.BrowserUnit;
import co.cartoon.fun.comics.R;

public class GridAdapter extends BaseDynamicGridAdapter {
    private static class Holder {
        TextView title;
        ImageView cover;
    }

    private final List<GridItem> list;
    public List<GridItem> getList() {
        return list;
    }

    private final Context context;

    public GridAdapter(Context context, List<GridItem> list, int columnCount) {
        super(context, list, columnCount);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            holder = new Holder();
            holder.title = view.findViewById(R.id.grid_item_title);
            holder.cover = view.findViewById(R.id.grid_item_cover);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        GridItem item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.cover.setImageBitmap(BrowserUnit.file2Bitmap(context, item.getFilename()));

        return view;
    }
}
