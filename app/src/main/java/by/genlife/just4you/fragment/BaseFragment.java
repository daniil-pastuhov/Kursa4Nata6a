package by.genlife.just4you.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import by.genlife.just4you.activity.BaseActivity;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class BaseFragment extends Fragment {

    public void setActionBarTitle(int resId) {
        if (getActivity() != null && ((BaseActivity) getActivity()).getSupportActionBar() != null)
            ((BaseActivity) getActivity()).getSupportActionBar().setTitle(resId);
    }

    protected void hideKeyboard() {
        BaseActivity activity = (BaseActivity) getActivity();

        if (activity == null || activity.hasBeenDestroyed()) {
            return;
        }

        activity.hideKeyboard();
    }

    protected void showKeyboard(View view) {
        if (getActivity() == null || ((BaseActivity) getActivity()).hasBeenDestroyed() || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    @Override
    public void onDestroyView() {
        hideKeyboard();

        super.onDestroyView();
    }

    public BaseActivity getMainActivity() {
        return (BaseActivity) getActivity();
    }

}
