package com.beetech.serialport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beetech.serialport.R;
import com.beetech.serialport.code.response.ReadDataResponse;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReadDataRvAdapter extends RecyclerView.Adapter<ReadDataRvAdapter.ViewHolder> {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");

    List<ReadDataResponse> mList;

    public ReadDataRvAdapter(List<ReadDataResponse> data) {
        this.mList = data;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_data_list_item, parent, false);
        ReadDataRvAdapter.ViewHolder viewHolder = new ReadDataRvAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReadDataRvAdapter.ViewHolder holder, int position) {
        ReadDataResponse readData = mList.get(position);
        holder.tvId.setText(readData.get_id()+"");

        holder.tvTemp.setText(readData.getTemp()+"");
        holder.tvTemp1.setText(readData.getTemp1()+"");
        holder.tvTemp2.setText(readData.getTemp2()+"");
        holder.tvTemp3.setText(readData.getTemp3()+"");
        holder.tvTemp4.setText(readData.getTemp4()+"");
        holder.tvTemp5.setText(readData.getTemp5()+"");
        holder.tvTemp6.setText(readData.getTemp6()+"");
        holder.tvTemp7.setText(readData.getTemp7()+"");

        holder.tvRh.setText(readData.getRh()+"");
        holder.tvRh1.setText(readData.getRh1()+"");
        holder.tvRh2.setText(readData.getRh2()+"");
        holder.tvRh3.setText(readData.getRh3()+"");
        holder.tvRh4.setText(readData.getRh4()+"");
        holder.tvRh5.setText(readData.getRh5()+"");
        holder.tvRh6.setText(readData.getRh6()+"");
        holder.tvRh7.setText(readData.getRh7()+"");

        holder.tvSensorDataTime.setText(dateFormat.format(readData.getSensorDataTime()));
        holder.tvSendFlag.setText(readData.getSendFlag() == 0 ? "否" : "是");
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId;

        public TextView tvTemp;
        public TextView tvTemp1;
        public TextView tvTemp2;
        public TextView tvTemp3;
        public TextView tvTemp4;
        public TextView tvTemp5;
        public TextView tvTemp6;
        public TextView tvTemp7;

        public TextView tvRh;
        public TextView tvRh1;
        public TextView tvRh2;
        public TextView tvRh3;
        public TextView tvRh4;
        public TextView tvRh5;
        public TextView tvRh6;
        public TextView tvRh7;

        public TextView tvSensorDataTime;
        public TextView tvSendFlag;

        public ViewHolder(View convertView) {
            super(convertView);

            tvId = (TextView) convertView.findViewById(R.id.tvId);

            tvTemp = (TextView) convertView.findViewById(R.id.tvTemp);
            tvTemp1 = (TextView) convertView.findViewById(R.id.tvTemp1);
            tvTemp2 = (TextView) convertView.findViewById(R.id.tvTemp2);
            tvTemp3 = (TextView) convertView.findViewById(R.id.tvTemp3);
            tvTemp4 = (TextView) convertView.findViewById(R.id.tvTemp4);
            tvTemp5 = (TextView) convertView.findViewById(R.id.tvTemp5);
            tvTemp6 = (TextView) convertView.findViewById(R.id.tvTemp6);
            tvTemp7 = (TextView) convertView.findViewById(R.id.tvTemp7);

            tvRh = (TextView) convertView.findViewById(R.id.tvRh);
            tvRh1 = (TextView) convertView.findViewById(R.id.tvRh1);
            tvRh2 = (TextView) convertView.findViewById(R.id.tvRh2);
            tvRh3 = (TextView) convertView.findViewById(R.id.tvRh3);
            tvRh4 = (TextView) convertView.findViewById(R.id.tvRh4);
            tvRh5 = (TextView) convertView.findViewById(R.id.tvRh5);
            tvRh6 = (TextView) convertView.findViewById(R.id.tvRh6);
            tvRh7 = (TextView) convertView.findViewById(R.id.tvRh7);

            tvSensorDataTime = (TextView) convertView.findViewById(R.id.tvSensorDataTime);

            tvSendFlag = (TextView) convertView.findViewById(R.id.tvSendFlag);
        }
    }

}
