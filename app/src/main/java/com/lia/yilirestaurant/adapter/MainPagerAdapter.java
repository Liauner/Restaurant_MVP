package com.lia.yilirestaurant.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lia.yilirestaurant.fragment.MeFragment;
import com.lia.yilirestaurant.fragment.CommunicationFragment;
import com.lia.yilirestaurant.fragment.EntranceFragment;
import com.lia.yilirestaurant.fragment.FoodFragment;
import com.lia.yilirestaurant.listener.FragmentListener;
import com.lia.yilirestaurant.util.BaseApplication;

public class MainPagerAdapter extends FragmentPagerAdapter implements FragmentListener {
    private final FragmentManager mFragmentManager;
    private Fragment mFragmentAtPositon0, mFragmentAtPositon1, mFragmentAtPositon2;
    public int pCurrentPosition = 0;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;

    }

    public int getpCurrentPosition() {
        return pCurrentPosition;
    }

    public void setpCurrentPosition(int pCurrentPosition) {
        this.pCurrentPosition = pCurrentPosition;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (mFragmentAtPositon0 == null) {
                mFragmentAtPositon0 = new FoodFragment(this);
            }
            return mFragmentAtPositon0;
        } else if (position == 1) {
            if (mFragmentAtPositon1 == null) {
                if (BaseApplication.getgUsername().isEmpty()) {
                    mFragmentAtPositon1 = new EntranceFragment(this, 0);
                } else {
                    mFragmentAtPositon1 = new CommunicationFragment(this, this);
                }
            }
            return mFragmentAtPositon1;
        } else if (position == 2) {
            if (mFragmentAtPositon2 == null) {
                if (BaseApplication.getgUsername().isEmpty()) {
                    mFragmentAtPositon2 = new EntranceFragment(this, 1);
                } else {
                    mFragmentAtPositon2 = new MeFragment(this);
                }
            }
            return mFragmentAtPositon2;
        } else
            return null;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if ((object instanceof EntranceFragment) && (pCurrentPosition == 0 || pCurrentPosition == 1 || pCurrentPosition == 2)) {
            return POSITION_NONE;
        }
        if ((object instanceof CommunicationFragment || object instanceof MeFragment) && (mFragmentAtPositon1 instanceof EntranceFragment || mFragmentAtPositon2 instanceof EntranceFragment) && (pCurrentPosition == 1 || pCurrentPosition == 2))
            return POSITION_NONE;
        if (object instanceof EntranceFragment && (mFragmentAtPositon1 instanceof CommunicationFragment || mFragmentAtPositon2 instanceof MeFragment) && (pCurrentPosition == 1 || pCurrentPosition == 2))
            return POSITION_NONE;
        if (object instanceof CommunicationFragment && pCurrentPosition == 1) {
            ((CommunicationFragment) object).updata();
        } else if (object instanceof MeFragment && pCurrentPosition == 2) {
            ((MeFragment) object).updata();
        }
        return super.getItemPosition(object);
    }


    @Override
    public void onSwitchToAnotherFragment(int requestCode, int resultCode) {
        if (requestCode == 0 || requestCode == 2) {
            mFragmentManager.beginTransaction().remove(mFragmentAtPositon1).remove(mFragmentAtPositon2).commitNow();
            mFragmentAtPositon1 = new CommunicationFragment(this, this);
            mFragmentAtPositon2 = new MeFragment(this);
            notifyDataSetChanged();
        } else if (requestCode == 1) {
            mFragmentManager.beginTransaction().remove(mFragmentAtPositon1).remove(mFragmentAtPositon2).commitNow();
            mFragmentAtPositon1 = new EntranceFragment(this, 0);
            mFragmentAtPositon2 = new EntranceFragment(this, 1);
            notifyDataSetChanged();
        }
    }
}
