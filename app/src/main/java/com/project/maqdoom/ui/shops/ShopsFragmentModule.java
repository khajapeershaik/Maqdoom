package com.project.maqdoom.ui.shops;

import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryAdapter;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryFragment;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ShopsFragmentModule {

    @Provides
    InsideCountryAdapter provideInsideCountryAdapter() {
        return new InsideCountryAdapter(new ArrayList<>());
    }

    @Provides
    LinearLayoutManager provideLayoutManager(ShopsFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }
}
