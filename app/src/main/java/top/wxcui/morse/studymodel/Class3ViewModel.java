package top.wxcui.morse.studymodel;




import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

import top.wxcui.morse.Word;
import top.wxcui.morse.WordRepository;


public class Class3ViewModel extends AndroidViewModel {
    private LiveData<List<Word>> words;
    private WordRepository wordRepository;
    private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();
    public void selectItem(String item) {
        selectedItem.setValue(item);
    }
    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }

    public Class3ViewModel(@NonNull Application application) {
        super(application);
      wordRepository=new WordRepository(application);
      words=wordRepository.getAllWords();
    }

    // TODO: Implement the ViewModel
public LiveData<List<Word>> getWords(){
    return words;
}
private void loadWords(){
    // Do an asynchronous operation to fetch Words.
}

}