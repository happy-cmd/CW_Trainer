package top.wxcui.morse.studymodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.wxcui.morse.R;

import top.wxcui.morse.Word;

public class wordlistadadapter_class3 extends RecyclerView.Adapter<wordlistadadapter_class3.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words
    private OnItemClickListener mOnItemClickListener;



    public wordlistadadapter_class3(FragmentActivity activity) {


        mInflater = LayoutInflater.from(activity);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,List<Word> mWords);

        ;
    }



    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.class3_text_show, parent, false);

        return new WordViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
        }
    }


    public void setWords(List<Word> words){
        mWords = words;
        notifyDataSetChanged();
    }
    //default：即不加任何访问修饰符，通常称为“默认访问模式“。
    // 该模式下，只允许在同一个包中进行访问。
    //Notifies the attached observers that
    // the underlying data has been changed and
    // any View reflecting the data set should refresh itself.

    public Word getWordAtPosition (int position) {
        return mWords.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.Class3_textView_show);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, mWords);
            }
        }

    }
}


