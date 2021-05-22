package top.wxcui.morse.studymodel;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import top.wxcui.morse.R;


public class Class1 extends Fragment {

    private Class1ViewModel mViewModel;
   private  ListView listView;

    public Class1(Class1ViewModel class1ViewModel) {
        this.mViewModel=class1ViewModel;
    }


//    public static Class1 newInstance() {
//        return new Class1();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
      View MainView=inflater.inflate(R.layout.class1_fragment, container, false);
         listView=MainView.findViewById(R.id.Class_Lesson1);

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
              //  Toast.makeText(getActivity(), "selected Item Name is " + m.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


}