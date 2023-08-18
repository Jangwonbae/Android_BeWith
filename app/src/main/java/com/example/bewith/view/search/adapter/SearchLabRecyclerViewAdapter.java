package com.example.bewith.view.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bewith.R;
import com.example.bewith.databinding.SearchLabListBinding;
import com.example.bewith.view.search.data.ProfessorData;
import java.util.ArrayList;

public class SearchLabRecyclerViewAdapter extends RecyclerView.Adapter<SearchLabRecyclerViewAdapter.ViewHolder> {

    private static SearchLabRecyclerViewAdapter.OnItemClickListener itemClickListener;
    private ArrayList<ProfessorData> localDataSet;

    //인터페이스 선언
    public interface OnItemClickListener {
        //클릭시 동작할 함수
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(SearchLabRecyclerViewAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //===== 뷰홀더 클래스 =====================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        SearchLabListBinding binding;

        public ViewHolder(SearchLabListBinding binding) {
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
    public SearchLabRecyclerViewAdapter(ArrayList<ProfessorData> dataSet) {
        localDataSet = dataSet;
    }
    //--------------------------------------------------

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public SearchLabRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_lab_list, parent, false);
        SearchLabRecyclerViewAdapter.ViewHolder viewHolder = new SearchLabRecyclerViewAdapter.ViewHolder(SearchLabListBinding.bind(view));

        return viewHolder;
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull SearchLabRecyclerViewAdapter.ViewHolder holder, int position) {


        String name = localDataSet.get(position).name;
        String department = localDataSet.get(position).department;
        String tel = localDataSet.get(position).tel;
        String lab = localDataSet.get(position).lab;
        String email = localDataSet.get(position).email;

        holder.binding.nameTextView.setText(name);
        holder.binding.departmentTextView.setText(department);
        holder.binding.telTextView.setText(tel);
        holder.binding.labTextView.setText(lab);
        holder.binding.emailTextView.setText(email);

    }

    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {
        return localDataSet.size();
    }
}
