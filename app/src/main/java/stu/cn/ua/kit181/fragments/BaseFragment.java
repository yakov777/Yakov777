package stu.cn.ua.kit181.fragments;
import android.os.Bundle;
import android.content.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import stu.cn.ua.kit181.contract.AppContract;
import stu.cn.ua.kit181.contract.ResponseListener;

public class BaseFragment extends Fragment {
    private AppContract appContract;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.appContract = (AppContract) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.appContract.unregisterListeners(this);
        this.appContract = null;
    }
    final AppContract getAppContract() {
        return appContract;
    }
    final <T> void registerListener(Class<T> clazz,
                                    ResponseListener<T> listener) {
        getAppContract().registerListener(this, clazz, listener);
    }
}