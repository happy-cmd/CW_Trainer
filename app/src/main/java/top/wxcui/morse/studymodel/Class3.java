package top.wxcui.morse.studymodel;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.List;

import top.wxcui.morse.R;
import top.wxcui.morse.Word;


public class Class3 extends Fragment {

    private Class3ViewModel mViewModel;
    private RecyclerView recyclerView;

    public Class3(Class3ViewModel class3ViewModel) {
        this.mViewModel=class3ViewModel;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.class3_fragment, container, false);
        recyclerView=mView.findViewById(R.id.Class3_recyclerview);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
        wordlistadadapter_class3 adapter=new wordlistadadapter_class3(getActivity());
        //返回class3中点击的值
        adapter.setOnItemClickListener(new wordlistadadapter_class3.OnItemClickListener() {
            @Override
            public void onItemClick(View view, List<Word> mWords) {
                int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                Word current = mWords.get(childAdapterPosition);
                mViewModel.selectItem(current.getWord());
              //  Toast.makeText(getActivity(), "item content = "+current.getWord(), Toast.LENGTH_SHORT).show();
            }


        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewModel.getWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });

    }



}