package com.example.project_skripsi.utils.service.zoom

import android.content.Context
import us.zoom.sdk.*

class ZoomService {

    companion object {
        val inst = ZoomService()
    }

    fun initializeSdk(context: Context, listener: ZoomSDKInitializeListener) {
        val sdk = ZoomSDK.getInstance()
        // TODO: Do not use hard-coded values for your key/secret in your app in production!
        val params = ZoomSDKInitParams().apply {
            appKey =
                "PjTZvGwvtyFX5BE4ijxlt4jhDF0dPoqWq33k" // TODO: Retrieve your SDK key and enter it here
            appSecret =
                "8pAVjzORnNM1DsJAsJUzC4L89XGP3RHmUXqS" // TODO: Retrieve your SDK secret and enter it here
            domain = "zoom.us"
            enableLog = true // Optional: enable logging for debugging
        }
        // TODO (optional): Add functionality to this listener (e.g. logs for debugging)
//        val listener = object : ZoomSDKInitializeListener {
//            /**
//             * If the [errorCode] is [ZoomError.ZOOM_ERROR_SUCCESS], the SDK was initialized and can
//             * now be used to join/start a meeting.
//             */
//            override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) = Unit
//            override fun onZoomAuthIdentityExpired() = Unit
//        }
        sdk.initialize(context, listener, params)
    }

    fun joinMeeting(context: Context, name: String?, meetingNumber: String? = null, pw: String? = null) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams().apply {
            displayName = name ?: "anonymous"
            meetingNo = "89463658239"
            password = "H3cNgS"
        }
        meetingService.joinMeetingWithParams(context, params, options)
    }


}