package edu.uw.tcss450.cfb3.asynctaskdemoupdated.ui;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.navigation.Navigation;
import edu.uw.tcss450.cfb3.asynctaskdemoupdated.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClickMeFragment extends Fragment {

    // A task to run in the background.
    private WaitAsyncTask mTask;

    // Needed to handle back button presses. When task is active and the progress bar is visible,
    // back presses should cancel the task.
    private OnBackPressedCallback mBackCallback;

    public ClickMeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_click_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_click_me_click)
                .setOnClickListener(this::onButtonPressed);

        // The callback should be disabled until task becomes active so send false to the
        // constructor.
        mBackCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                if (mTask != null) {
                    mTask.cancel(true);
                }
            }
        };

        // attach the back pressed listener (back) to the Activity.
        // Whats the difference between getActivity and requireActivity?
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(this, mBackCallback);
    }

    public void onButtonPressed(final View buttonClicked) {
        mTask = new WaitAsyncTask();
        mTask.execute();
    }

    private class WaitAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().findViewById(R.id.button_click_me_click).setEnabled(false);
            getView().findViewById(R.id.layout_click_me_wait).setVisibility(View.VISIBLE);
            mBackCallback.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ((TextView) getView().findViewById(R.id.textview_click_me_progress))
                    .setText("Task " + values[0] + " is complete.");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            getView().findViewById(R.id.button_click_me_click).setEnabled(true);
            getView().findViewById(R.id.layout_click_me_wait).setVisibility(View.GONE);
            mBackCallback.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getView().findViewById(R.id.button_click_me_click).setEnabled(true);
            getView().findViewById(R.id.layout_click_me_wait).setVisibility(View.GONE);
            mBackCallback.setEnabled(false);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_clickMeFragment_to_doneFragment);
        }
    }
}
