package top.wxcui.morse.studymodel;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import top.wxcui.morse.R;
import top.wxcui.morse.Word;

public class Class2 extends Fragment {

    private Class2ViewModel mViewModel;
    private ListView listView;

    public Class2(Class2ViewModel class2ViewModel) {
        this.mViewModel=class2ViewModel;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View MainView= inflater.inflate(R.layout.class2_fragment, container, false);
        listView=MainView.findViewById(R.id.Class_Lesson2);

        return MainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object m=parent.getItemAtPosition(position);
                mViewModel.selectItem(m.toString());
            //    Toast.makeText(getActivity(), "selected Item Name is " + m.toString(), Toast.LENGTH_LONG).show();
            }
        });



    }


}