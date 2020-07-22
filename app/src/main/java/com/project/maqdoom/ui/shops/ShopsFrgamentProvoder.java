package com.project.maqdoom.ui.shops;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ShopsFrgamentProvoder {
    @ContributesAndroidInjector(modules = ShopsFragmentModule.class)
    abstract ShopsFragment provideShopFragmentFactory();
}
