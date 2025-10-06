package dev.carlosmuvi.common.di

import dev.carlosmuvi.common.model.AIModel
import dev.carlosmuvi.common.repository.AIEventParserRepository
import dev.carlosmuvi.common.repository.AIModelRepository
import dev.carlosmuvi.common.repository.EventParserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    // Register AIModelRepository with injected models
    single { AIModelRepository(get<List<AIModel>>()) }

    // Register AI-powered EventParserRepository
    singleOf(::AIEventParserRepository) bind EventParserRepository::class
}