/*
 * Copyright 2020 Maqdoom. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.maqdoom.ui.customerTouristGuide;

import com.project.maqdoom.ui.touristGuideDetails.TouristGuidesDetailsFragment;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.Module;
import dagger.Provides;


@Module
public class TouristGuideFragmentModule {

    @Provides
    TouristGuideAdapter provideTouristGuideAdapter() {
        return new TouristGuideAdapter(new ArrayList<>());
    }
    @Provides
    LinearLayoutManager provideLinearLayoutManager(TouristGuidesFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }

}
