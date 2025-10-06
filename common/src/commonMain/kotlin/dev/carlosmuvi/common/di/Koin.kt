package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule())
    }

// called by iOS etc
fun initKoin() = initKoin(appDeclaration = {})

fun commonModule() = module {
    // Add your dependencies here

    includes(repositoryModule)
    includes(viewModelModule)
    includes(platformModule())
}
