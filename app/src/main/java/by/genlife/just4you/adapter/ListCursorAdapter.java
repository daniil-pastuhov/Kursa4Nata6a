package by.genlife.just4you.adapter;

/**
 * Created by NotePad.by on 21.02.2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.genlife.just4you.R;
import by.genlife.just4you.activity.WordActivity;
import by.genlife.just4you.model.Word;
import by.genlife.just4you.word.WordDAO;


public class ListCursorAdapter extends CursorRecyclerViewAdapter<ListCursorAdapter.ViewHolder> {

    private Context mContext;
    private WordDAO mWordDAO;

    public ListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
        mWordDAO = new WordDAO(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View parent;
        public TextView mName;
        public TextView mTranslation;
        public ImageView mFavorite;

        public ViewHolder(View view) {
            super(view);
            parent = view;
            mName = (TextView) view.findViewById(R.id.name);
            mTranslation = (TextView) view.findViewById(R.id.translation);
            mFavorite = (ImageView) view.findViewById(R.id.favorite);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        itemView.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWordDAO.changeFavorite((long) v.getTag());
                changeCursor(mWordDAO.findAll());
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordActivity.startActivity(mContext, (long) v.getTag());
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Word word = mWordDAO.buildObject(cursor);
        viewHolder.mName.setText(word.name);
        StringBuilder translations = new StringBuilder();
        for (int i = 0; i < 2 && i < word.translations.size(); i++) {
            translations.append(word.translations.get(i)).append("\n");
        }
        translations.append("...");
        viewHolder.mTranslation.setText(translations.toString());
        viewHolder.mFavorite.setImageResource(word.isFavorite ? R.drawable.star_fill : R.drawable.star);
        viewHolder.mFavorite.setTag(word.id);
        viewHolder.parent.setTag(word.id);
    }
}
