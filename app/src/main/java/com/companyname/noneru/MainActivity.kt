package com.companyname.noneru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.companyname.noneru.data.RetrySupportingOfferRepository
import org.koin.android.ext.android.get
import org.koin.android.scope.ext.android.getOrCreateScope
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity() {
    lateinit var retrySupportingOfferRepo: RetrySupportingOfferRepository
    private lateinit var repositoryScope: Scope

    lateinit var offerDisplayCaseView: OfferDisplayCaseView
    lateinit var offerDisplayCaseViewModel: OfferDisplayCaseViewModel
    lateinit var networkDetector: NetworkDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryScope = getOrCreateScope("repositoryScope")
        networkDetector = get()
        offerDisplayCaseView = get()
        offerDisplayCaseViewModel = getViewModel()
        retrySupportingOfferRepo = get()

        networkDetector.onCreate(this)
        offerDisplayCaseView.onCreate(offerDisplayCaseViewModel, this)

        networkDetector.addListener(retrySupportingOfferRepo::notifyConnectionIsRestored)
    }


    override fun onDestroy() {
        super.onDestroy()
        if(!isChangingConfigurations) {
            repositoryScope.close()
        }
        networkDetector.onDestroy(this)
    }
}
