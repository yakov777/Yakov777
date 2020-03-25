package stu.cn.ua.kit181.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import stu.cn.ua.kit181.R;
import stu.cn.ua.kit181.model.Student;
import ua.cn.stu.getvariant.GetVariantTasks;
import ua.cn.stu.getvariant.RetainManager;
import ua.cn.stu.getvariant.Task;
import ua.cn.stu.getvariant.TaskListener;

public class ResultsFragment extends BaseFragment {
    private static final String TAG = ResultsFragment.class.getSimpleName();
    private static final String ARG_STUDENT = "STUDENT";
    private static final String KEY_TASK = "GET_VARIANT_TASK";
    private ViewGroup resultsTable;
    private Button doneButton;
    private Button tryAgainButton;
    private ProgressBar progress;
    private TextView variantTextView;
    private TextView errorTextView;
    private RetainManager retainManager;
    private Task<Integer> getVariantTask;
    public static ResultsFragment newInstance(Student student) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_STUDENT, student);
        ResultsFragment fragment = new ResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results,
                container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultsTable = view
                .findViewById(R.id.resultsTable);
        doneButton = view
                .findViewById(R.id.doneButton);
        tryAgainButton = view
                .findViewById(R.id.tryAgainButton);
        progress = view
                .findViewById(R.id.progress);
        variantTextView = view
                .findViewById(R.id.variantTextView);
        errorTextView = view
                .findViewById(R.id.errorTextView);
        TextView firstNameTextView = view
                .findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = view
                .findViewById(R.id.lastNameTextView);
        TextView groupTextView = view
                .findViewById(R.id.groupTextView);
        Student student = getStudent();
        firstNameTextView.setText(student.getFirstName());
        lastNameTextView.setText(student.getLastName());
        groupTextView.setText(student.getGroup());
        doneButton.setOnClickListener(v -> getAppContract().cancel());
        tryAgainButton.setOnClickListener(v -> fetchVariant(true));
        fetchVariant(false);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getVariantTask.setListener(null);
        if (isRemoving()) {
            retainManager.destroy();
        }
    }
    private void fetchVariant(boolean create) {
        toPendingState();
        retainManager = new RetainManager(this);
        if (create) retainManager.delete(KEY_TASK);
        getVariantTask = retainManager.getOrCreate(KEY_TASK,
                () -> createGetVariantTask().execute());
        getVariantTask.setListener(new TaskListener<Integer>() {
            @Override
            public void onSuccess(Integer variant) {
                toSuccessState(variant);
            }
            @Override
            public void onError(Throwable error) {
                toErrorState(error);
            }
        });
    }
    private Task<Integer> createGetVariantTask() {
        Student student = getStudent();
        return GetVariantTasks.createGetVariantTask(
                this,
                student.getFirstName(),
                student.getLastName(),
                student.getGroup(),
                Student.MAX_VARIANT
        );
    }
    private void toPendingState() {
        progress.setVisibility(View.VISIBLE);
        resultsTable.setVisibility(View.INVISIBLE);
        doneButton.setVisibility(View.INVISIBLE);
        tryAgainButton.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
    }
    private void toSuccessState(int variant) {
        doneButton.setVisibility(View.VISIBLE);
        tryAgainButton.setVisibility(View.GONE);
        resultsTable.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        variantTextView.setText(String.valueOf(variant));
    }
    private void toErrorState(Throwable error) {
        Log.e(TAG, "Error!", error);
        doneButton.setVisibility(View.INVISIBLE);
        tryAgainButton.setVisibility(View.VISIBLE);
        resultsTable.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        if (error instanceof IOException) {
            errorTextView.setText(R.string.error);
        } else {
            errorTextView.setText(error.getMessage());
        }
    }
    private Student getStudent() {
        return getArguments().getParcelable(ARG_STUDENT);
    }
        }
