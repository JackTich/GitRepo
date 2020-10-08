package com.jacktich.gitrepo.ui.repositories

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jacktich.gitrepo.R
import com.jacktich.gitrepo.data.api.apihelpers.Status
import com.jacktich.gitrepo.data.api.responces.DataOwner
import com.jacktich.gitrepo.data.api.responces.ErrorRepositoriesResponse
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.ui.repositories.adapter.AdapterRepositories
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_repositories.*
import javax.inject.Inject

class RepositoriesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var repositoriesViewModel: RepositoriesViewModel

    private var adapterRepositories = AdapterRepositories()
    private lateinit var userRepositories: List<RepositoriesItem>

    private val allReposTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            if (p0 != null) {
                repositoriesViewModel.getSearchRepositories(p0)
            } else {
                adapterRepositories.submitList(null)
                repositoriesViewModel.getPublicRepositories()
            }
            return false
        }

    }

    private val userReposTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            if (p0 != null) {
                val filteredList = userRepositories.filter { it.fullName.contains(p0, true) }
                adapterRepositories.submitList(filteredList)
            } else {
                adapterRepositories.submitList(userRepositories)
            }
            return false
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_repos_toolbar, menu)
        val searchItem = menu!!.findItem(R.id.searchViewItem)
        val searchView = searchItem.actionView as SearchView
        if (repositoriesViewModel.isAuthorizing()) {
            searchView.setOnQueryTextListener(userReposTextListener)
        } else {
            searchView.setOnQueryTextListener(allReposTextListener)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        AndroidInjection.inject(this)
        Toast.makeText(this, "this", Toast.LENGTH_SHORT).show()
        firstInit()
        observeRepositories()
    }

    private fun firstInit() {
        repositoriesViewModel =
            ViewModelProvider(this, viewModelFactory).get(RepositoriesViewModel::class.java)
        repositoriesViewModel.getPublicRepositories().value?.data?.let {
            Log.i("item", it[0].fullName)
        }
        rvRepositories.apply {
            adapter = adapterRepositories
            layoutManager = LinearLayoutManager(this@RepositoriesActivity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

    }

    private fun observeRepositories() {
        if (repositoriesViewModel.isAuthorizing()) {
            repositoriesViewModel.getUserRepositories().observe(this, {
                userRepositories = it
                adapterRepositories.apply {
                    submitList(currentList)
                }
            })
        } else {
            repositoriesViewModel.getPublicRepositories().observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        adapterRepositories.apply {
                            submitList(currentList + it.data as List<RepositoriesItem>)
                        }
                    }
                    Status.ERROR -> {
                        if (it.message != null) Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(this, "yyy", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            )
        }
    }

    private fun getRepositoriesShimmerList(): List<RepositoriesItem> {
        val listShimmer = mutableListOf<RepositoriesItem>()
        for (i in 0..99) {
            listShimmer.add(
                RepositoriesItem(
                    isShimmer = true,
                    id = -1,
                    fullName = "",
                    owner = DataOwner(null),
                    description = ""
                )
            )
        }
        return listShimmer
    }

}