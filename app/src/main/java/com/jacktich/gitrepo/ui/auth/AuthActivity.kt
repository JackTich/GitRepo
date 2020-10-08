package com.jacktich.gitrepo.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jacktich.gitrepo.BuildConfig
import com.jacktich.gitrepo.R
import com.jacktich.gitrepo.data.api.responces.ErrorAuthResponse
import com.jacktich.gitrepo.data.api.responces.ErrorRepositoriesResponse
import com.jacktich.gitrepo.data.api.responces.TokenResponse
import com.jacktich.gitrepo.ui.repositories.RepositoriesActivity
import com.jacktich.gitrepo.utils.ApiConst
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        AndroidInjection.inject(this)
        authViewModel = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)
        onClickActivity()
    }

    override fun onResume() {
        super.onResume()
        val catchUri = intent.data
        if (catchUri != null && catchUri.toString().startsWith(BuildConfig.REDIRECT_URI)){
            getAccessToken(catchUri.getQueryParameter("code")!!)
        }
    }

    private fun onClickActivity() {

        btnShowAllRepo.setOnClickListener {
            openReposActivity()
        }

        btnMakeAuthorization.setOnClickListener {
            makeAuthorization()
        }
    }

    private fun openReposActivity() {
        val intent = Intent(this, RepositoriesActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun makeAuthorization() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(ApiConst.URL_AUTH + ApiConst.PARAM_CLIENT_ID + BuildConfig.CLIENT_ID + ApiConst.PARAM_SCOPE + ApiConst.PARAM_REDIRECT_URI + BuildConfig.REDIRECT_URI)
        )
        startActivity(intent)
    }

    private fun getAccessToken(code: String){
        authViewModel.getAccessToken(code).observe(this, {
            when(it){
                is TokenResponse -> {
                    authViewModel.putTokenInPref(it.tokenType + " " + it.accessToken)
                    openReposActivity()
                }
                is ErrorAuthResponse -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


}