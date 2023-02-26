package com.raccoonsquare.dating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.raccoonsquare.dating.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class FinderFragment extends Fragment implements Constants {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private SectionsPagerAdapter adapter;

    public FinderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_finder, container, false);

        getActivity().setTitle(R.string.nav_spotlight);

        mViewPager = rootView.findViewById(R.id.view_pager);

        adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SpotlightFragment(), getString(R.string.nav_spotlight));
        adapter.addFragment(new PeopleNearbyFragment(), getString(R.string.nav_people_nearby));
        adapter.addFragment(new SearchFragment(), getString(R.string.nav_search));
        adapter.addFragment(new HotgameFragment(), getString(R.string.nav_hotgame));
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                getActivity().setTitle(mViewPager.getAdapter().getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {

            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (mViewPager.getCurrentItem()) {

            case 0: {

                PeopleNearbyFragment p = (PeopleNearbyFragment) adapter.getItem(0);
                p.onRequestPermissionsResult(requestCode, permissions, grantResults);

                break;
            }

            case 2: {

                HotgameFragment p = (HotgameFragment) adapter.getItem(2);
                p.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

            default: {

                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}