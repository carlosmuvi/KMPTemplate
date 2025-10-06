package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.model.AIModel
import dev.carlosmuvi.common.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    aiModels: List<AIModel> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) =
    startKoin {
        appDeclaration()
        modules(commonModule(aiModels))
    }

// called by iOS etc
fun initKoin(aiModels: List<AIModel>) =
    initKoin(aiModels, appDeclaration = {})

fun commonModule(aiModels: List<AIModel> = emptyList()) = module {
    // Add your dependencies here

    // Register the list of AI models
    single { aiModels }

    includes(repositoryModule)
    includes(viewModelModule)
    includes(platformModule())
}
