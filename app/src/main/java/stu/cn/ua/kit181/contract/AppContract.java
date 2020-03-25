package stu.cn.ua.kit181.contract;
import android.os.*;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import stu.cn.ua.kit181.model.Student;

public interface AppContract {
    void toOptionsScreen(Fragment target, @Nullable Student student);
    void toResultsScreen(Fragment target, Student student);
    /**
     * Exit from the current screen
     */
    void cancel();
    /**
     * Publish results to the target screen
     */
    <T> void publish(T data);
    /**
     * Listen for results from other screens
     */
    <T> void registerListener(Fragment fragment, Class<T> clazz, ResponseListener<T> listener);
    /**
     * Stop listening for results from other screens
     */
    void unregisterListeners(Fragment fragment);
}
