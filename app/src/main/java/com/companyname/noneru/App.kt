package com.companyname.noneru

import android.app.Application
import com.companyname.noneru.data.N1Api
import com.companyname.noneru.data.OfferRepository
import com.companyname.noneru.data.RetrySupportingOfferRepository
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val dataSourceModule = module {
            single<N1Api> {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.n1.ru/api/v1/offers/")
                    .build()!!
                retrofit.create(N1Api::class.java)
            }
        }

        val displayCaseModules = module {

            factory { NetworkDetector() }

            scope("repositoryScope") {
                RetrySupportingOfferRepository(get())
            } bind OfferRepository::class

            factory {
                OfferDisplayCaseView()
            }
            viewModel {
                OfferDisplayCaseViewModel(get())
            }
        }
        val appModules = listOf(dataSourceModule, displayCaseModules)

        startKoin(this, appModules)
    }
}