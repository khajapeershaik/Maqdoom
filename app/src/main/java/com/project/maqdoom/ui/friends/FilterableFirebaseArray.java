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

package com.project.maqdoom.ui.friends;

import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.HashSet;
import java.util.Set;

public class FilterableFirebaseArray extends FirebaseArray {

    private Set<String> excludes = new HashSet<>();

    /*public FilterableFirebaseArray(Query query, Class aClass) {
        super(query, aClass);
    }*/

    public FilterableFirebaseArray(Query query, SnapshotParser parser) {
        super(query, parser);
    }

    public void addExclude(String key) {
        excludes.add(key);
    }

    public void removeExclude(String key) {
        excludes.remove(key);
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        if (!excludes.contains(snapshot.getKey())) {
            super.onChildAdded(snapshot, excludes.contains(previousChildKey)? null : previousChildKey);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
        if (!excludes.contains(snapshot.getKey())) {
            super.onChildChanged(snapshot, excludes.contains(previousChildKey)? null : previousChildKey);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        if (!excludes.contains(snapshot.getKey())) {
            super.onChildMoved(snapshot, excludes.contains(previousChildKey)? null : previousChildKey);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
        if (!excludes.contains(snapshot.getKey())) {
            super.onChildRemoved(snapshot);
        }
    }

}
