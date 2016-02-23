package by.genlife.just4you.model;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by NotePad.by on 23.02.2016.
 */
public class Topic extends AbstractModel {
    private static final long serialVersionUID = 2L;
    public static final String SEP = ";";

    public String name = "";
    public List<String> themes;

    public String serializeThemes() {
        if (themes == null || themes.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String theme : themes) {
            stringBuilder.append(theme).append(SEP);
        }
        stringBuilder.setLength(stringBuilder.length() - SEP.length());
        return stringBuilder.toString();
    }

    public void parseThemes(String themesStr) {
        if (TextUtils.isEmpty(themesStr)) themes = Collections.emptyList();
        else themes = Arrays.asList(themesStr.split(SEP));
    }
}
