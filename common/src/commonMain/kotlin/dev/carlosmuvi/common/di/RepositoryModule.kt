package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.repository.AIEventParserRepository
import dev.carlosmuvi.common.repository.AIModelRepository
import dev.carlosmuvi.common.repository.EventParserRepository
import dev.carlosmuvi.common.repository.createAIModelRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    // Register platform-specific AIModelRepository
    single<AIModelRepository> { createAIModelRepository() }

    // Register AI-powered EventParserRepository
    singleOf(::AIEventParserRepository) bind EventParserRepository::class
}