package by.genlife.just4you.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import by.genlife.just4you.R;
import by.genlife.just4you.model.Word;
import by.genlife.just4you.word.WordDAO;

/**
 * Created by NotePad.by on 22.02.2016.
 */
public class WordPageFragment extends Fragment {
    private static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private Word word;
    private boolean isFavorite;
    private EditText nameEdit;
    private EditText translationEdit;
    private WordDAO mWordDAO;
    private boolean isNew;

    public static WordPageFragment newInstance(long page) {
        WordPageFragment pageFragment = new WordPageFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWordDAO = new WordDAO(getContext());
        long idWord = getArguments().getLong(ARGUMENT_PAGE_NUMBER);
        if (idWord == 0) {
            isNew = true;
            return;
        }
        word = mWordDAO.find(idWord);

        isFavorite = word.isFavorite;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_page, null);

        nameEdit = (EditText) view.findViewById(R.id.name);
        translationEdit = (EditText) view.findViewById(R.id.translation);

        if (!isNew)
            initWithData();
        else word = new Word();

        ImageView fav = (ImageView) view.findViewById(R.id.favorite);
        fav.setImageResource(word.isFavorite ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                ((ImageView)v).setImageResource(isFavorite ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
            }
        });
        return view;
    }

    private void initWithData() {
        nameEdit.setText(word.name);
        StringBuilder builder = new StringBuilder();
        for (String translation : word.translations) {
            builder.append(translation).append("\n");
        }
        if (builder.length() > 1)
            builder.setLength(builder.length() - 1);
        translationEdit.setText(builder.toString());
    }

    @Override
    public void onDestroyView() {
        word.isFavorite = isFavorite;
        word.name = nameEdit.getText().toString();
        word.translations = parseTranses(translationEdit.getText().toString());
        mWordDAO.insertOrUpdate(word);
        getContext().sendBroadcast(new Intent(WordsFragment.ACTION_DATA_CHANGED));
        super.onDestroyView();
    }

    private List<String> parseTranses(String string) {
        return Arrays.asList(string.split("\n"));
    }
}
