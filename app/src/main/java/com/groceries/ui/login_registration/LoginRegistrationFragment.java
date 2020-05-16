package com.groceries.ui.login_registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.groceries.R;

import java.util.ArrayList;

public class LoginRegistrationFragment extends Fragment {

    public LoginRegistrationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_registration, container, false);

        ViewPager pager = view.findViewById(R.id.view_pager);
        TabLayout tabs = view.findViewById(R.id.view_pager_tabs);
        LoginFragment loginFragment = new LoginFragment();
        RegistrationFragment registrationFragment = new RegistrationFragment();
        PagerAdapter adapter = new PagerAdapter(getFragmentManager());
        tabs.setupWithViewPager(pager);

        adapter.add(loginFragment);
        adapter.add(registrationFragment);
        pager.setAdapter(adapter);

        return view;
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void add(Fragment fragment) {
            this.fragments.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Login";
            } else {
                return "Registration";
            }
        }
    }
}
