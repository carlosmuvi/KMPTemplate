package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.model.AIModel
import dev.carlosmuvi.common.model.AIModels
import dev.carlosmuvi.common.platformModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

// iOS uses this - Swift factory will provide AI models module
fun initKoin(onKoinStart: KoinApplication.() -> Unit): KoinApplication =
    startKoin {
        onKoinStart()
        modules(commonModule)
    }

val commonModule = module {
    includes(repositoryModule)
    includes(viewModelModule)
    includes(platformModule())
}

