package com.mindmachine.appmodule.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jetpackframework.base.BaseFragment;
import com.mindmachine.appmodule.fragment.DeviceFreeTcFragment;
import com.mindmachine.appmodule.fragment.DeviceFreeZyFragment;

import java.util.ArrayList;
import java.util.List;

public class DevicePageAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> frags;
    public DevicePageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        frags = new ArrayList<>();
        frags.add(new DeviceFreeTcFragment());
        frags.add(new DeviceFreeZyFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return frags.size();
    }
}
