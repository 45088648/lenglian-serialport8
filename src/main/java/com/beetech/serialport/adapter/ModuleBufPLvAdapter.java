package com.beetech.serialport.adapter;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beetech.serialport.R;
import com.beetech.serialport.bean.ModuleBuf;
import java.text.SimpleDateFormat;

public class ModuleBufPLvAdapter extends PagedListAdapter<ModuleBuf, ModuleBufPLvAdapter.ViewHolder> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    PagedList<ModuleBuf> mPagedList;

    public ModuleBufPLvAdapter(PagedList<ModuleBuf> mPagedList){
        super(new DiffCallback<ModuleBuf>() {

            @Override
            public boolean areItemsTheSame(@NonNull ModuleBuf oldItem, @NonNull ModuleBuf newItem) {
                Log.d("DiffCallback", "areItemsTheSame");
                return oldItem.get_id() == newItem.get_id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ModuleBuf oldItem, @NonNull ModuleBuf newItem) {
                Log.d("DiffCallback", "areContentsTheSame");
                return oldItem.get_id() == newItem.get_id();
            }
        });
        this.mPagedList = mPagedList;
    }

    private class DataBean {
        public int id;
        public String content;
    }

    @Override
    public int getItemCount() {
        return mPagedList.size();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_buf_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModuleBuf moduleBuf = mPagedList.get(position);
        holder.tvId.setText(moduleBuf.get_id()+"");
        holder.tvType.setText(moduleBuf.getType() == 0 ? "写" : "读");
        String cmd = "其他";
        switch (moduleBuf.getCmd()){
            case 1:
                cmd = "配置";
                break;
             case 4:
                cmd = "授时";
                break;
             case 7:
                cmd = "数据";
                break;
            default:
                cmd = "其他";
                break;
        }
        holder.tvCmd.setText(cmd);
        holder.tvBuf.setText(moduleBuf.getBufHex());
        holder.tvResult.setText(moduleBuf.isResult() ? "成功" : "失败");
        holder.tvInputTime.setText(dateFormat.format(moduleBuf.getInputTime()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId;
        public TextView tvType;
        public TextView tvCmd;
        public TextView tvBuf;
        public TextView tvResult;
        public TextView tvInputTime;

        public ViewHolder(View convertView) {
            super(convertView);

            tvId = (TextView) convertView.findViewById(R.id.tvId);
            tvType = (TextView) convertView.findViewById(R.id.tvType);
            tvCmd = (TextView) convertView.findViewById(R.id.tvCmd);
            tvBuf = (TextView) convertView.findViewById(R.id.tvBuf);
            tvResult = (TextView) convertView.findViewById(R.id.tvResult);
            tvInputTime = (TextView) convertView.findViewById(R.id.tvInputTime);
        }
    }

}
