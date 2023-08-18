package com.example.bewith.view.community.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bewith.R;
import com.example.bewith.databinding.ReplyListBinding;
import com.example.bewith.view.community.data.ReplyData;


import java.util.ArrayList;

public class ReplyRecyclerViewAdapter extends RecyclerView.Adapter<ReplyRecyclerViewAdapter.ViewHolder> {

    private static ReplyRecyclerViewAdapter.OnItemClickListener itemClickListener;
    private ArrayList<ReplyData> localDataSet;
    private String communityUUID;
    private String myUUID;

    //인터페이스 선언
    public interface OnItemClickListener {
        //클릭시 동작할 함수
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(ReplyRecyclerViewAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //===== 뷰홀더 클래스 =====================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ReplyListBinding binding;

        public ViewHolder(ReplyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    //----- 생성자 --------------------------------------
    // 생성자를 통해서 데이터를 전달받도록 함
    public ReplyRecyclerViewAdapter(ArrayList<ReplyData> dataSet, String communityUUID, String myUUID) {
        localDataSet = dataSet;
        this.communityUUID = communityUUID;
        this.myUUID = myUUID;
    }
    //--------------------------------------------------

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public ReplyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reply_list, parent, false);
        ReplyRecyclerViewAdapter.ViewHolder viewHolder = new ReplyRecyclerViewAdapter.ViewHolder(ReplyListBinding.bind(view));

        return viewHolder;
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull ReplyRecyclerViewAdapter.ViewHolder holder, int position) {

        if(localDataSet.get(position).ReplyUUID.equals(myUUID)){
            holder.binding.whoTextView.setTextColor(Color.parseColor("#1E90FF"));
        }
        else{
            holder.binding.whoTextView.setTextColor(Color.parseColor("#595959"));
        }
        if(localDataSet.get(position).ReplyUUID.equals(communityUUID)){
            holder.binding.whoTextView.setText((localDataSet.get(position).nickname+"(작성자)"));
        }else{
            holder.binding.whoTextView.setText(localDataSet.get(position).nickname);
        }
        holder.binding.contentsTextView.setText(localDataSet.get(position).ReplyText);
        holder.binding.timeTextView.setText(localDataSet.get(position).ReplyTime);

    }

    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {
        return localDataSet.size();
    }
}
