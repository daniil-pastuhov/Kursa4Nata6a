package by.genlife.just4you.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.genlife.just4you.R;
import by.genlife.just4you.utils.AppUtils;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class MainFragment extends BaseFragment {

    private TextView mVersionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        mVersionTextView = (TextView) view.findViewById(R.id.version_textview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        setActionBarTitle(R.string.app_name);
        mVersionTextView.setText(getString(R.string.version_number, AppUtils.getAppVersionName(getActivity())));
    }

}
