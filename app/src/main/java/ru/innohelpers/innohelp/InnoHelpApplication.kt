package ru.innohelpers.innohelp

import android.app.Application
import android.os.Handler
import android.os.Looper
import io.realm.Realm
import io.realm.RealmConfiguration
import ru.innohelpers.innohelp.realm.RealmModule
import ru.innohelpers.innohelp.services.DaggerServicesComponent
import ru.innohelpers.innohelp.services.ServicesComponent
import ru.innohelpers.innohelp.services.modules.ProvidersModule
import ru.innohelpers.innohelp.services.modules.ServerModule
import ru.innohelpers.innohelp.services.modules.StorageModule
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class InnoHelpApplication : Application() {

    override fun onCreate() {
        val realmThread = Thread {
            lock.withLock {
                Looper.prepare()
                mHandler = Handler(Looper.myLooper()!!)
                Realm.init(this)
                val configuration = RealmConfiguration.Builder()
                    .name("cache.realm")
                    .modules(RealmModule())
                    .deleteRealmIfMigrationNeeded()
                    .schemaVersion(1)
                    .build()
                Realm.setDefaultConfiguration(configuration)
                defaultRealm = Realm.getDefaultInstance()
                println("created:" + Thread.currentThread().id)
            }
            Looper.loop()
        }
        realmThread.start()
        super.onCreate()
    }

    companion object {
        private var mHandler: Handler? = null
        private var defaultRealm: Realm? = null

        private var component: ServicesComponent? = null
        private val lock = ReentrantLock()

        val servicesComponent: ServicesComponent
            get() {
                if (component == null)
                    lock.withLock {
                        component = DaggerServicesComponent.builder()
                            .providersModule(ProvidersModule())
                            .serverModule(ServerModule())
                            .storageModule(StorageModule(mHandler!!, defaultRealm!!))
                            .build()
                    }
                return component!!
            }
    }
}