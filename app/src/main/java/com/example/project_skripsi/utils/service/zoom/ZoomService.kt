package com.example.project_skripsi.utils.service.zoom

import android.content.Context
import us.zoom.sdk.*

class ZoomService {

    companion object {
        val inst = ZoomService()
    }

    fun initializeSdk(context: Context, listener: ZoomSDKInitializeListener) {
        val sdk = ZoomSDK.getInstance()
        val params = ZoomSDKInitParams().apply {
            appKey =
                "PjTZvGwvtyFX5BE4ijxlt4jhDF0dPoqWq33k"
            appSecret =
                "8pAVjzORnNM1DsJAsJUzC4L89XGP3RHmUXqS"
            domain = "zoom.us"
            enableLog = true // Optional: enable logging for debugging
        }
        sdk.initialize(context, listener, params)
    }

    fun joinMeeting(context: Context, name: String?) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams().apply {
            displayName = name ?: "anonymous"
            meetingNo = "89463658239"
            password = "H3cNgS"
        }
        meetingService.joinMeetingWithParams(context, params, options)
    }

    /**
     * For further development use this instead
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
     */
}