package by.genlife.just4you.model;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class Word extends AbstractModel {

    private static final long serialVersionUID = 1L;
    public static final String SEP = ";";

    public String name = "";
    public List<String> translations;
    public boolean isFavorite;

    public String serializeTranslations() {
        if (translations == null || translations.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String translation : translations) {
            stringBuilder.append(translation).append(SEP);
        }
        stringBuilder.setLength(stringBuilder.length() - SEP.length());
        return stringBuilder.toString();
    }

    public void parseTranslations(String translationsStr) {
        if (TextUtils.isEmpty(translationsStr)) translations = Collections.emptyList();
        else translations = Arrays.asList(translationsStr.split(SEP));
    }
}
