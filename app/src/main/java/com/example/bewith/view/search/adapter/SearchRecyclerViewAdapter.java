package com.example.bewith.view.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bewith.R;
import com.example.bewith.databinding.SearchListBinding;
import com.example.bewith.view.search.data.ClassData;
import java.util.ArrayList;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private static SearchRecyclerViewAdapter.OnItemClickListener itemClickListener;
    private ArrayList<ClassData> localDataSet;

    //인터페이스 선언
    public interface OnItemClickListener {
        //클릭시 동작할 함수
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(SearchRecyclerViewAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //===== 뷰홀더 클래스 =====================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        SearchListBinding binding;

        public ViewHolder(SearchListBinding binding) {
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
    public SearchRecyclerViewAdapter(ArrayList<ClassData> dataSet) {
        localDataSet = dataSet;
    }
    //--------------------------------------------------

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list, parent, false);
        SearchRecyclerViewAdapter.ViewHolder viewHolder = new SearchRecyclerViewAdapter.ViewHolder(SearchListBinding.bind(view));

        return viewHolder;
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull SearchRecyclerViewAdapter.ViewHolder holder, int position) {

        String department = localDataSet.get(position).department;
        String grade = localDataSet.get(position).grade;
        String division = localDataSet.get(position).division;
        String className = localDataSet.get(position).className;
        String professor = localDataSet.get(position).professor;
        String timePlace = localDataSet.get(position).timePlace;

        holder.binding.departmentTextView.setText(department);
        holder.binding.gradeTextView.setText(grade);
        holder.binding.divisionTextView.setText(division);
        holder.binding.classNameTextView.setText(className);
        holder.binding.professorTextView.setText(professor);
        holder.binding.timePlaceTextView.setText(timePlace);

    }

    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {
        return localDataSet.size();
    }
}
