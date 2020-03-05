package com.tudny.wkdapp.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

@Navigator.Name("fragment")
public class KeepStateNavigator extends FragmentNavigator {

	private FragmentManager manager;
	private int containerId;
	private Context context;

	public KeepStateNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
		super(context, manager, containerId);
		this.manager = manager;
		this.containerId = containerId;
		this.context = context;
	}

	@Nullable
	@Override
	public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {

		String tag = Integer.toString(destination.getId());
		FragmentTransaction transaction = manager.beginTransaction();

		boolean initialNavigate = false;
		Fragment currentFragment = manager.getPrimaryNavigationFragment();
		if(currentFragment != null) {
			transaction.detach(currentFragment);
		} else {
			initialNavigate = true;
		}

		Fragment fragment = manager.findFragmentByTag(tag);
		if(fragment == null){
			String className = destination.getClassName();
			fragment = manager.getFragmentFactory().instantiate(context.getClassLoader(), className);
			transaction.add(containerId, fragment, tag);
		} else {
			transaction.attach(fragment);
		}

		transaction.setPrimaryNavigationFragment(fragment);
		transaction.setReorderingAllowed(true);
		transaction.commitNow();

		if(initialNavigate) {
			return destination;
		} else {
			return null;
		}
	}
}
