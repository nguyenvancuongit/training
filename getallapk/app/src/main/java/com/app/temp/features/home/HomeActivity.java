package com.app.temp.features.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;

import com.app.temp.R;
import com.app.temp.base.activity.BaseActivity;
import com.app.temp.base.adapter.MenuAdapter;
import com.app.temp.features.home.repolist.RepoListFragment;
import com.app.temp.views.ToolbarView;
import com.app.temp.features.register.RegisterFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    ToolbarView toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.lv_menu)
    ListView lvMenu;

    public MenuAdapter menuAdapter;
    public HashMap<String, String> mFragmentName;

    @Override
    public void initTagLog() {
        TAG = "HomeActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // init
        initToolbar();
        initFragmentsName();
        initMenuData();

        // init first screen
        transactionFragment(RepoListFragment.newInstance());
    }

    private void initToolbar() {
        // listener
        toolbar.getImgMenu().setOnClickListener(view -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    public void initFragmentsName() {
        mFragmentName = new HashMap<>();
        mFragmentName.put(RepoListFragment.class.getSimpleName(), getString(R.string.fragment_name_home));
        mFragmentName.put(Home2Fragment.class.getSimpleName(), getString(R.string.fragment_name_home_2));
        mFragmentName.put(RegisterFragment.class.getSimpleName(), getString(R.string.fragment_name_register));
    }

    public void initMenuData() {
        List<String> menuList = new ArrayList<>();
        menuList.add(getString(R.string.fragment_name_home));
        menuList.add(getString(R.string.fragment_name_home_2));
        menuList.add(getString(R.string.fragment_name_register));
        menuAdapter = new MenuAdapter(this, menuList);
        lvMenu.setAdapter(menuAdapter);

        // set menu click event
        lvMenu.setOnItemClickListener((adapterView, view, position, id) -> {
            if (menuAdapter.getItem(position).equals(getString(R.string.fragment_name_home))) {
                transactionFragment(RepoListFragment.newInstance());
            } else if (menuAdapter.getItem(position).equals(getString(R.string.fragment_name_home_2))) {
                transactionFragment(Home2Fragment.newInstance());
            } else if (menuAdapter.getItem(position).equals(getString(R.string.fragment_name_register))) {
                transactionFragment(RegisterFragment.newInstance());
            }
            drawer.closeDrawer(GravityCompat.START);
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            int size = getSupportFragmentManager().getBackStackEntryCount();
            if (fragmentList != null && size > 0) {
                setUI(getCurrentlyFragment());
            } else {
                super.onBackPressed();  // if activity doesn't attach any fragment, finish the activity
            }
        }
    }

    /**
     * replace fragment
     *
     * @param fragment fragment for replace
     */
    public void transactionFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.fragment_content, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
        setUI(fragment);
    }

    /**
     * set title for toolbar from list fragment name
     *
     * @param fragment currently fragment
     */
    private void setUI(Fragment fragment) {
        if (fragment != null) {
            toolbar.setTitle(getFragmentName(fragment.getClass().getSimpleName()));
        }
    }

    /**
     * use to get toolbar title from mFragmentName (list fragment name're saved)
     *
     * @param simpleName fragment class name
     * @return fragment title
     */
    private String getFragmentName(String simpleName) {
        for (Object name : mFragmentName.keySet()) {
            String title = mFragmentName.get(name);
            if (name.equals(simpleName)) {
                return title;
            }
        }
        return "Missing Name";
    }

    /**
     * @return currently fragment
     */
    public Fragment getCurrentlyFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        int size = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentList != null && size > 0) {
            return fragmentList.get(size - 1);
        } else {
            return null;
        }
    }
}
