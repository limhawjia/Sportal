package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.R;

public class SocialSwipeViewFragment extends Fragment {
    private static final String USER_UID_TAG = "user_uid";
    private static String mUserUid;

    public SocialSwipeViewFragment() {
    }

    public static SocialSwipeViewFragment newInstance(String userUid) {
        SocialSwipeViewFragment fragment = new SocialSwipeViewFragment();
        Bundle args = new Bundle();
        args.putString(USER_UID_TAG, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserUid = getArguments().getString(USER_UID_TAG);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Timber.d("created search swipe view");

        View view = inflater.inflate(R.layout.social_swipe_view_fragment, container, false);
        SocialSwipeViewPagerAdapter adapter = new SocialSwipeViewPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        ViewPager viewPager = view.findViewById(R.id.social_swipe_view);
        TabLayout tabLayout = view.findViewById(R.id.social_tab_layout);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    static class SocialSwipeViewPagerAdapter extends FragmentPagerAdapter {
        private static final String[] pageTitles = new String[] { "FRIENDS", "REQUESTS", "CHATS" };

        SocialSwipeViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new SocialFriendsFragment();
                    break;
                case 1:
                    fragment = SocialRequestsFragment.newInstance(mUserUid);
                    break;
                case 2:
                    fragment = SocialChatsFragment.newInstance(mUserUid);
                    break;
                default:
                    fragment = new SocialFriendsFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return pageTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles[position];
        }
    }
}
