package com.makeshift.whattimeisitthere

import android.app.Application

class WhatTimeIsItThereApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        WhenaboutRepository.initialize(this)
    }
}