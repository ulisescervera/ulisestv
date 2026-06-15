package com.gmail.uli153.ulisestv

import android.app.Application
import com.gmail.uli153.ulisestv.data.di.dataModule
import com.gmail.uli153.ulisestv.di.useCaseModule
import com.gmail.uli153.ulisestv.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class UlisesTvApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@UlisesTvApplication)
            modules(
                dataModule,
                useCaseModule,
                viewModelModule,
            )
        }
    }
}
