package ai.benshi.test.ui

import ai.benshi.test.databinding.FragmentSettingsBinding
import ai.benshi.test.preferences.PrefManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var prefManager: PrefManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        val view = binding.root
        prefManager = PrefManager(requireContext())

        if(prefManager.email.isNotEmpty()){
            binding.currentEmailTxt.text = "Current email address: ${prefManager.email}"
        }

        binding.addEmailBtn.setOnClickListener {
            if(binding.emailAddressEtxt.text.trim().isNotEmpty()){
                val email = binding.emailAddressEtxt.text.toString()
                prefManager.email = email
                binding.currentEmailTxt.text = "Current email address: $email"
                binding.emailAddressEtxt.text.clear()
                Toast.makeText(requireContext(),  "$email saved" , Toast.LENGTH_SHORT).show()

            }
        }
        return view
    }

}