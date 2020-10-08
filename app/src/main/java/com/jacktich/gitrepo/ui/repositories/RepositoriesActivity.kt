package com.jacktich.gitrepo.ui.repositories

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacktich.gitrepo.R
import com.jacktich.gitrepo.data.api.responces.DataOwner
import com.jacktich.gitrepo.data.api.responces.RepositoriesItem
import com.jacktich.gitrepo.di.base.CustomSavedStateViewModelFactory
import com.jacktich.gitrepo.ui.auth.AuthActivity
import com.jacktich.gitrepo.ui.repositories.adapter.AdapterRepositories
import com.jacktich.gitrepo.utils.ApiConst
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_repositories.*
import javax.inject.Inject

class RepositoriesActivity : AppCompatActivity() {

    @Inject
    lateinit var customSavedStateFactory: CustomSavedStateViewModelFactory

    private lateinit var repositoriesViewModel: RepositoriesViewModel

    private var adapterRepositories = AdapterRepositories()

    private val recyclerViewOnScrollListener =
        object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentReposCount = repositoriesViewModel.currentRepositoryCount.value
                val lastVisibleItemPosition =
                    (rvRepositories.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (currentReposCount != null && lastVisibleItemPosition >= currentReposCount - 3) {
                    repositoriesViewModel.currentRepositoryCount.value =
                        currentReposCount + ApiConst.PAGE_COUNT
                    repositoriesViewModel.getPublicRepositories(adapterRepositories.getLatestItemId())
                    adapterRepositories.submitList(adapterRepositories.currentList + getRepositoriesShimmerList())
                }
            }
        }

    private val allReposTextListener =
        object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != "") {
                    rvRepositories.clearOnScrollListeners()
                    repositoriesViewModel.etFieldText.value = p0
                    adapterRepositories.submitList(getRepositoriesShimmerList())
                    repositoriesViewModel.getSearchRepositories(p0!!)
                } else {
                    repositoriesViewModel.currentRepositoryCount.value =
                        ApiConst.PAGE_COUNT.toLong()
                    repositoriesViewModel.etFieldText.value = null
                    repositoriesViewModel.liveRepositoriesItem.value = listOf()
                    rvRepositories.addOnScrollListener(recyclerViewOnScrollListener)
                    repositoriesViewModel.getPublicRepositories()
                }
                return false
            }

        }

    private val userReposTextListener =
        object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                repositoriesViewModel.allUserRepositories.value?.let {
                    val allItemList = repositoriesViewModel.allUserRepositories.value
                    if (p0 != "") {
                        repositoriesViewModel.etFieldText.value = p0
                        repositoriesViewModel.liveRepositoriesItem.value =
                            allItemList?.filter { it.fullName.contains(p0!!, true) }

                    } else {
                        repositoriesViewModel.etFieldText.value = null
                        repositoriesViewModel.liveRepositoriesItem.value =
                            allItemList
                    }
                }
                return false
            }

        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_repos_toolbar, menu)
        val searchItem = menu!!.findItem(R.id.searchViewItem)
        val overflowMenuItem = menu.findItem(R.id.overflowItem)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        val isAuthorizing = repositoriesViewModel.isAuthorizing()

        //Create overflow menu
        overflowMenuItem.title = if (isAuthorizing) {
            getString(R.string.sign_out)
        } else {
            getString(R.string.sign_in)
        }
        overflowMenuItem.setOnMenuItemClickListener {
            if (isAuthorizing) {
                repositoriesViewModel.clearToken()
                openAuthActivity()
            } else {
                openAuthActivity(isRequiredLogin = true)
            }
            return@setOnMenuItemClickListener true
        }

        //Create SearchView
        repositoriesViewModel.etFieldText.value?.let {
            searchView.setQuery(it, true)
            searchItem.isActionViewExpanded
            searchView.requestFocus()
        }
        if (isAuthorizing) {
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
        firstInit()
        observeRepositories()
    }

    private fun firstInit() {
        val localFactory = customSavedStateFactory.create(this)
        repositoriesViewModel =
            ViewModelProvider(this, localFactory).get(RepositoriesViewModel::class.java)
        rvRepositories.apply {
            adapter = adapterRepositories
            layoutManager = LinearLayoutManager(this@RepositoriesActivity)
        }
        if (!repositoriesViewModel.isAuthorizing()) {
            rvRepositories.addOnScrollListener(recyclerViewOnScrollListener)
        }
        if (repositoriesViewModel.liveRepositoriesItem.value == null) {
            adapterRepositories.submitList(getRepositoriesShimmerList())
        }
    }

    private fun observeRepositories() {

        repositoriesViewModel.liveRepositoriesItem.observe(this, {
            adapterRepositories.apply {
                submitList(it)
            }
        })

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

    private fun openAuthActivity(isRequiredLogin: Boolean? = null) {
        val intent = Intent(this, AuthActivity::class.java)
        isRequiredLogin?.let {
            intent.putExtra(AuthActivity.KEY_REQUIRED_LOGIN, it)
        }
        startActivity(intent)
        finish()
    }

}