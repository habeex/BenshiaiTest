package ai.benshi.test.ui

import ai.benshi.test.R
import ai.benshi.test.adapter.PostsAdapter
import ai.benshi.test.adapter.PostsLoadStateAdapter
import ai.benshi.test.adapter.PostsViewHolder
import ai.benshi.test.adapter.asMergedLoadStates
import ai.benshi.test.databinding.FragmentHomeBinding
import ai.benshi.test.model.Post
import ai.benshi.test.preferences.PrefManager
import ai.benshi.test.utils.Utils
import ai.benshi.test.viewmodel.MainViewModel
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment(), MenuProvider, PostsViewHolder.ClickListener {
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PostsAdapter
    lateinit var prefManager: PrefManager
    lateinit var deviceId: String

    override fun onResume() {
        super.onResume()
        Objects.requireNonNull((requireActivity() as MainActivity).supportActionBar)
            ?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        prefManager = PrefManager(requireContext())
        deviceId = Utils.getDeviceId(requireContext())

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        initAdapter()
        initSwipeToRefresh()
        return  view
    }

    private fun initAdapter() {
        adapter = PostsAdapter(mainViewModel, this)
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PostsLoadStateAdapter(adapter),
            footer = PostsLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collect { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.getPosts.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect { binding.recyclerView.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.settings -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            }
            else -> false
        }
    }

    override fun onItemClicked(post: Post) {
       lifecycleScope.launch {
           val email = prefManager.email
           mainViewModel.logUserAction(
               email = email,
               deviceId = deviceId,
               action = "open",
               postId = post.id,
           )
       }
    }

}