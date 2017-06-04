package charview.com.dragrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import charview.com.mychartview.R;

/**
 * Created by lin on 2017/5/29.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Subject> datas;
    private Context mContext;
    private LayoutInflater mLiLayoutInflater;

    public MyAdapter(List<Subject> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
        this.mLiLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLiLayoutInflater.inflate(R.layout.item_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_title.setText(datas.get(position).getTitle());
        holder.img.setImageResource(datas.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
