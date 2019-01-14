package com.companyname.noneru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.companyname.noneru.data.Offer
import kotlinx.serialization.protobuf.ProtoBuf

class DetailedOfferActivity : AppCompatActivity() {
    object Params {
        val article = "article"
    }

    lateinit var offerView: OfferView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_offer_layout)

        offerView = OfferView(this, findViewById(R.id.main))

        offerView.onCreate(unpackArticleFromExtras())
    }


    private fun unpackArticleFromExtras(): Offer {
        val buffer = intent.extras!!.getByteArray(Params.article)
        val serializer = Offer.serializer()
        return ProtoBuf.load(serializer, buffer!!)
    }
}
