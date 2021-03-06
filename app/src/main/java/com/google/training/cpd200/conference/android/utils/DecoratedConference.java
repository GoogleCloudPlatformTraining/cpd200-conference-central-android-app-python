/* Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.training.cpd200.conference.android.utils;


import com.appspot.cpd200_extras.conference.Conference;
import com.appspot.cpd200_extras.conference.model.ModelsConferenceForm;

/**
 * A wrapper around the {@link com.appspot.cpd200_extras.conference.model.ModelsConferenceForm}
 * to enable adding additional fields and operations.
 */
public class DecoratedConference {

    private ModelsConferenceForm mConference;
    private boolean mRegistered;

    public DecoratedConference(ModelsConferenceForm conference, boolean registered) {
        mConference = conference;

        /* keeps the status of user's attendance to this conference */
        mRegistered = registered;
    }

    public ModelsConferenceForm getConference() {
        return mConference;
    }

    public void setConference(ModelsConferenceForm conference) {
        mConference = conference;
    }

    public boolean isRegistered() {
        return mRegistered;
    }

    public void setRegistered(boolean registered) {
        mRegistered = registered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DecoratedConference that = (DecoratedConference) o;

        if (mConference != null ? !mConference.equals(that.mConference)
                : that.mConference != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return mConference != null ? mConference.hashCode() : 0;
    }
}
