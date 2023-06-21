package com.example.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull ContainerScheduleList fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){

        switch (position){
            case 0:
                return new Container1();
            case 1:
                return new Container2();
            default:
                return new Container1();

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
