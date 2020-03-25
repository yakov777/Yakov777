package stu.cn.ua.kit181.fragments;
import android.os.Bundle;
import android.os.*;
import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import stu.cn.ua.kit181.R;
import stu.cn.ua.kit181.model.Student;

public class OptionsFragment extends BaseFragment {
    private static final String ARG_STUDENT = "STUDENT";
    private static final String KEY_GROUP = "GROUP";
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Spinner groupsSpinner;
    public static OptionsFragment newInstance(
            @Nullable Student student) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_STUDENT, student);
        OptionsFragment fragment = new OptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_options,
                container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstNameEditText = view
                .findViewById(R.id.firstNameEditText);
        lastNameEditText = view
                .findViewById(R.id.lastNameEditText);
        groupsSpinner = view
                .findViewById(R.id.groupSpinner);
        setupButtons(view);
        String selectedGroup = null;
        if (savedInstanceState != null) {
// EditText fields save/restore input automatically
// so need to restore only Spinner data
            selectedGroup = savedInstanceState
                    .getString(KEY_GROUP);
        } else {
            Student student = getStudentArg();
            if (student != null) {
                firstNameEditText.setText(student
                        .getFirstName());
                lastNameEditText.setText(student.getLastName());
                selectedGroup = student.getGroup();
            }
        }
        setupGroupsSpinner(selectedGroup);
    }
    private void setupButtons(View view) {
        view.findViewById(R.id.cancelButton)
                .setOnClickListener(v -> {
                    getAppContract().cancel();
                });
        view.findViewById(R.id.doneButton)
                .setOnClickListener(v -> {
                    Student student = new Student(
                            firstNameEditText.getText().toString(),
                            lastNameEditText.getText().toString(),
                            groupsSpinner.getSelectedItem().toString()
                    );
                    if (!student.isValid()) {
                        Toast.makeText(
                                getContext(),
                                R.string.empty_fields_error,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    getAppContract().publish(student);
                    getAppContract().cancel();
                });
    }
    private void setupGroupsSpinner(
            @Nullable String selectedGroup) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.item_group,
                Student.GROUPS
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_dropdown_item_1line);
        groupsSpinner.setAdapter(adapter);
        if (selectedGroup != null) {
            int index = Student.GROUPS.indexOf(selectedGroup);
            if (index != -1) groupsSpinner.setSelection(index);
        }
    }
    private Student getStudentArg() {
        return getArguments().getParcelable(ARG_STUDENT);
    }
}
