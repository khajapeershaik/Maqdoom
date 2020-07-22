package com.project.maqdoom.ui.shopsAddPackage;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import dagger.Module;
import dagger.Provides;


@Module
public class ShopDetailFragmentModule {

    @Provides
    LinearLayoutManager provideLinearLayoutManager(ShopsAddFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }
}
