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

import android.content.Context;
import android.util.Log;

import com.appspot.cpd200_extras.conference.model.ModelsBooleanMessage;
import com.appspot.cpd200_extras.conference.model.ModelsConferenceForm;
import com.appspot.cpd200_extras.conference.model.ModelsConferenceForms;
import com.appspot.cpd200_extras.conference.model.ModelsProfileForm;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.training.cpd200.conference.android.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for communication with the Cloud Endpoint.
 */
public class ConferenceUtils {

    private final static String TAG = "ConferenceUtils";
    private static com.appspot.cpd200_extras.conference.Conference sApiServiceHandler;

    public static void build(Context context, String email) {
        sApiServiceHandler = buildServiceHandler(context, email);
    }

    /**
     * Returns a list of {@link DecoratedConference}s.
     * This list includes information about what {@link com.appspot.cpd200_extras.conference.model.ModelsConferenceForm}s
     * user has registered for.
     *
     * @return
     * @throws ConferenceException
     * @see <code>getProfile</code>
     */
    public static List<DecoratedConference> getConferences()
            throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "getConferences(): no service handler was built");
            throw new ConferenceException();
        }

        com.appspot.cpd200_extras.conference.Conference.QueryConferences
                queryConferences = sApiServiceHandler.queryConferences(null);
        ModelsConferenceForms conferenceCollection = queryConferences.execute();

        if (conferenceCollection != null && conferenceCollection.getItems() != null) {
            List<ModelsConferenceForm> conferences = conferenceCollection.getItems();
            List<DecoratedConference> decoratedList = null;
            if (null == conferences || conferences.isEmpty()) {
                return decoratedList;
            }
            decoratedList = new ArrayList<DecoratedConference>();
            ModelsProfileForm profile = getProfile();
            List<String> registeredConfKeys = null;
            if (null != profile) {
                registeredConfKeys = profile.getConferenceKeysToAttend();
            }
            if (null == registeredConfKeys) {
                registeredConfKeys = new ArrayList<String>();
            }
            for (ModelsConferenceForm conference : conferences) {
                DecoratedConference decorated = new DecoratedConference(conference,
                        registeredConfKeys.contains(conference.getWebsafeKey()));
                decoratedList.add(decorated);
            }
            return decoratedList;
        }
        return null;
    }

    /**
     * Registers user for a {@link com.appspot.cpd200_extras.conference.model.ModelsConferenceForm}
     *
     * @param conference
     * @return
     * @throws ConferenceException
     */
    public static boolean registerForConference(ModelsConferenceForm conference)
            throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "registerForConference(): no service handler was built");
            throw new ConferenceException();
        }

        com.appspot.cpd200_extras.conference.Conference.RegisterForConference
                registerForConference = sApiServiceHandler.registerForConference(
                conference.getWebsafeKey());
        ModelsBooleanMessage result = registerForConference.execute();
        return result.getData();
    }

    /**
     * Unregisters user from a {@link com.appspot.cpd200_extras.conference.model.ModelsConferenceForm}.
     *
     * @param conference
     * @return
     * @throws ConferenceException
     */
    public static boolean unregisterFromConference(ModelsConferenceForm conference)
            throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "unregisterFromConference(): no service handler was built");
            throw new ConferenceException();
        }

        /*com.appspot.cpd200_extras.conference.Conference.UnregisterFromConference
                unregisterFromConference = sApiServiceHandler.unregisterFromConference(
                conference.getWebsafeKey());
        */
        //ModelsBooleanMessage result = unregisterFromConference.execute();
        //return result.getResult();
        return false;
    }

    /**
     * Returns the user {@link com.appspot.cpd200_extras.conference.model.ModelsProfileForm}. Can
     * be used to find out what conferences user is registered for.
     *
     * @return
     * @throws ConferenceException
     */
    public static ModelsProfileForm getProfile() throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "getProfile(): no service handler was built");
            throw new ConferenceException();
        }

        com.appspot.cpd200_extras.conference.Conference.GetProfile getProfile =
                sApiServiceHandler.getProfile();
        return getProfile.execute();
    }

    /**
     * Build and returns an instance of {@link com.appspot.cpd200_extras.conference.Conference}
     *
     * @param context
     * @param email
     * @return
     */
    public static com.appspot.cpd200_extras.conference.Conference buildServiceHandler(
            Context context, String email) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                context, AppConstants.AUDIENCE);
        credential.setSelectedAccountName(email);

        com.appspot.cpd200_extras.conference.Conference.Builder builder
                = new com.appspot.cpd200_extras.conference.Conference.Builder(
                AppConstants.HTTP_TRANSPORT,
                AppConstants.JSON_FACTORY, credential);
        builder.setApplicationName("conference-central-server");
        return builder.build();
    }
}
