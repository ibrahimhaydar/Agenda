/*
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.mobiweb.ibrahim.agenda.utils.pushFirebase;

import android.content.Intent;
import android.os.Build;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Service called registrationToken refreshed.
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        Logg.d();

       // startService(new Intent(this, FcmTokenRegistrationService.class));

        try{
      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //    startForegroundService(new Intent(this, FcmTokenRegistrationService.class));
      //  } else {
            startService(new Intent(this, FcmTokenRegistrationService.class));
      //  }
        }catch (Exception e){}

    }
}
