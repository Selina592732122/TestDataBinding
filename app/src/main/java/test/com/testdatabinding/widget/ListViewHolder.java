package test.com.testdatabinding.widget;

import android.util.SparseArray;
import android.view.View;

/**
 * Description:
 * author 洪培林
 * date 2016/8/25 15:08
 * Email：rainyeveningstreet@gmail.com
 */
public class ListViewHolder {

    /**
     * ImageView view = ViewHolderGetter.get(convertView, R.id.imageView);
     *
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get (View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
