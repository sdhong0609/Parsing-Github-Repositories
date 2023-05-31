package com.hongstudio.parsing_github_repositories

import com.hongstudio.parsing_github_repositories.model.RepositoryListModel
import com.hongstudio.parsing_github_repositories.service.GithubRepositoryService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Retrofit

interface Car

data class SamsungCar @Inject constructor(
    val engine: Engine,
    val tire: Tire,
) : Car

interface Engine {
    fun print()
}

class GasolineEngine() : Engine {
    override fun print() {
        println("GasolineEngine")
    }
}

class ElectricEngine : Engine {
    override fun print() {
        println("ElectricEngine")
    }
}

interface Tire

class NormalTire : Tire

class SuperTire : Tire


//@Singleton
//@Component(modules = [
//    EngineModule::class,
//    TireModule::class,
//    CarModule::class,
//])
//interface CarComponent {
//    fun getCar(): SamsungCar
//}

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {
    @Singleton
    @Provides
    fun provideEngine(): Engine {
        return ElectricEngine()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object TireModule {
    @Singleton
    @Provides
    fun provideTire(): Tire {
        return NormalTire()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CarModule {
    @Binds
    abstract fun bindsCar(car: SamsungCar): Car
}

fun main() {
//    val carComponent = DaggerCarComponent.create()
//
//    (1..20).forEach {
//        val car = carComponent.getCar()
//        println(car)
//        car.engine.print()
//    }
}



@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val baseUrl = "https://api.github.com/"
        val contentType = "application/json"

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(MediaType.get(contentType)))
            .build()
    }

    @Singleton
    @Provides
    fun provideGithubRepositoryService(retrofit: Retrofit): GithubRepositoryService {
        return retrofit.create(GithubRepositoryService::class.java)
    }
}

interface RepoRepository {
    fun getRepoList(keyword: String): Call<RepositoryListModel>
}

class RepoRepositoryImpl @Inject constructor(
    private val githubService: GithubRepositoryService
) : RepoRepository {
    override fun getRepoList(keyword: String): Call<RepositoryListModel> {
        return githubService.getSearchedRepositoryList(keyword)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsRepoRepository(repoRepositoryImpl: RepoRepositoryImpl): RepoRepository
}
