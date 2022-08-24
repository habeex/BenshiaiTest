package ai.benshi.test.ui

import ai.benshi.test.R
import ai.benshi.test.adapter.CommentAdapter
import ai.benshi.test.databinding.FragmentPostDetailBinding
import ai.benshi.test.model.Post
import ai.benshi.test.preferences.PrefManager
import ai.benshi.test.utils.Constants
import ai.benshi.test.utils.Utils
import ai.benshi.test.utils.Utils.Companion.sha256
import ai.benshi.test.viewmodel.MainViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import coil.load
import coil.size.Scale
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.coroutines.launch
import java.util.*


class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter() }
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var collapsingToolBarLayout: CollapsingToolbarLayout
    lateinit var prefManager: PrefManager
    lateinit var deviceId: String

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Objects.requireNonNull((requireActivity() as MainActivity).supportActionBar)?.hide()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        val view = binding.root
        appBarLayout = binding.appBarLayout
        collapsingToolBarLayout = binding.collapsingToolbarLayout
        toolbar = binding.toolbar
        toolbar.title = ""
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        var navController = navHostFragment.navController

        setupWithNavController(collapsingToolBarLayout, toolbar, navController)

        prefManager = PrefManager(requireContext())
        deviceId = Utils.getDeviceId(requireContext())

        binding.commentRecyclerView.adapter = commentAdapter
        mainViewModel.selectedPost.observe(requireActivity()) {
            binding.postTitleTxt.text = it.title
            binding.bodyTxt.text = it.body?.trim()
            binding.postImageView.load("${Constants.IMAGE_BASE_URL}/${it?.title?.sha256()}/1200/800"){
                crossfade(true)
                crossfade(750)
                placeholder(R.drawable.ic_baseline_image_24)
                scale(Scale.FILL)
            }

            binding.userName.text = "${it!!.user.name} (@${it!!.user.username})"
            binding.emailTxt.text = it.user.email
            binding.phoneTxt.text = it.user.phone
            binding.websiteTxt.text = it.user.website

            binding.mapImageView.load("${Constants.STATIC_MAP}center=${it.user.address.geo.lat}%2c%20${it.user.address.geo.lng}&zoom=12&size=400x400&key=${Constants.API_KEY}"){
                crossfade(true)
                crossfade(750)
                placeholder(R.drawable.ic_baseline_image_24)
                scale(Scale.FILL)
            }

            lifecycleScope.launchWhenCreated {
                mainViewModel.getCommentsByPostId(it.id).observe(requireActivity()) { comments ->
                    comments.let {
                        commentAdapter.setData(comments)
                    }
                }
            }

            binding.emailLayout.setOnClickListener { it1 ->
                if(!it.user.email.isNullOrEmpty()) composeEmail(arrayOf(it.user.email!!))
            }

            binding.phoneLayout.setOnClickListener { it1 ->
                dialPhoneNumber(it.user.phone)
            }

            binding.websiteLayout.setOnClickListener { it1 ->
                openWebPage(it.user.website)
            }

            logUserAction(it)
        }

        return view
    }

    fun logUserAction(post: Post) {
        lifecycleScope.launch {
            val email = prefManager.email
            mainViewModel.logUserAction(
                email = email,
                deviceId = deviceId,
                action = "view",
                postId = post.id,
            )
        }
    }


    fun composeEmail(addresses: Array<String>) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
        }
        startActivity(intent)
    }

    fun dialPhoneNumber(phoneNumber: String?) {
         if(phoneNumber.isNullOrEmpty()) return
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)

    }

    fun openWebPage(url: String?) {
        if(url.isNullOrEmpty()) return
        var webpage: Uri = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url");
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }
}