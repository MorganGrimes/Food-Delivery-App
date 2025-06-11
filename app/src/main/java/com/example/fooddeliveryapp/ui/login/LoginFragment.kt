package com.example.fooddeliveryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentLoginBinding
import com.example.fooddeliveryapp.utils.EMAIL
import com.example.fooddeliveryapp.utils.ERROR_FACEBOOK
import com.example.fooddeliveryapp.utils.ERROR_TWITTER
import com.example.fooddeliveryapp.utils.FB_LOGIN
import com.example.fooddeliveryapp.utils.FIELDS
import com.example.fooddeliveryapp.utils.ID_NAME_EMAIL
import com.example.fooddeliveryapp.utils.LOGIN_CANCELED
import com.example.fooddeliveryapp.utils.LOGIN_ERROR
import com.example.fooddeliveryapp.utils.LOGIN_SUCCESS
import com.example.fooddeliveryapp.utils.NO_EMAIL
import com.example.fooddeliveryapp.utils.PUBLIC_PROFILE
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences.getIsLoggedIn
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences.saveEmail
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences.setLoggedIn
import com.example.fooddeliveryapp.utils.TWITTER_COM
import com.example.fooddeliveryapp.utils.UiUtils
import com.example.fooddeliveryapp.utils.WRONG_EMAIL_PSW
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setupLoginButtonState()
        setupListener()
        checkRememberMe()
        facebookLogin()
        twitterLogin()
        loginResponse()
    }

    private fun setupListener() {
        val navController = findNavController()
        binding.apply {
            loginForgotPasswordTv.setOnClickListener {
                navController.navigate(R.id.action_loginFragment_to_authFragment)
            }
            loginSignUpTv.setOnClickListener {
                navController.navigate(R.id.action_loginFragment_to_signupFragment)
            }

            loginBtn.setOnClickListener {
                val email = loginEmailEt.text.toString().trim()
                val password = loginPasswordEt.text.toString().trim()

                loginViewModel.login(email, password)
            }
        }
    }

    private fun loginResponse() {
        loginViewModel.loginResult.collectInLifecycle(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    val context = requireContext()
                    val email = binding.loginEmailEt.text.toString().trim()

                    if (binding.loginRememberCheckbox.isChecked) {
                        saveEmail(context, email)
                        setLoggedIn(context, true)
                    } else {
                        setLoggedIn(context, false)
                    }

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(requireContext(), WRONG_EMAIL_PSW, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun <T> StateFlow<T>.collectInLifecycle(lifecycleOwner: LifecycleOwner, collector: suspend (T) -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect { collector(it) }
            }
        }
    }

    private fun setupLoginButtonState() {
        binding.apply {
            val emailEditText = loginEmailEt
            val passwordEditText = loginPasswordEt
            val loginButton = loginBtn

            loginButton.isEnabled = false
            loginButton.setBackgroundColor(UiUtils.brownColor)

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val email = emailEditText.text.toString().trim()
                    val password = passwordEditText.text.toString().trim()

                    val isValid = email.isNotEmpty() && password.isNotEmpty()

                    loginButton.isEnabled = isValid
                    loginButton.setBackgroundColor(
                        if (isValid) requireContext().getColor(R.color.orange)
                        else UiUtils.brownColor
                    )
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            emailEditText.addTextChangedListener(textWatcher)
            passwordEditText.addTextChangedListener(textWatcher)
        }
    }

    private fun checkRememberMe() {
        val context = requireContext()
        if (getIsLoggedIn(context)) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun facebookLogin() {

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.i(FB_LOGIN, LOGIN_SUCCESS + { result.accessToken.token })
                    val request = GraphRequest.newMeRequest(result.accessToken) { obj, _ ->
                        try {
                            val email = obj?.getString(EMAIL)
                            val context = requireContext()
                            email?.let { saveEmail(context, it) }
                            setLoggedIn(context, true)

                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } catch (e: JSONException) {
                            Log.e(FB_LOGIN, ERROR_FACEBOOK, e)
                        }
                    }

                    val parameters = Bundle()
                    parameters.putString(FIELDS, ID_NAME_EMAIL)
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Log.i(FB_LOGIN, LOGIN_CANCELED)
                }

                override fun onError(error: FacebookException) {
                    Log.i(FB_LOGIN, LOGIN_ERROR + { error.message }, error)
                }
            })

        binding.facebookIconIv.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this@LoginFragment,
                listOf(EMAIL, PUBLIC_PROFILE)
            )
        }
    }

    private fun twitterLogin() {
        binding.twitterIconIv.setOnClickListener {
            val provider = OAuthProvider.newBuilder(TWITTER_COM)
            val firebaseAuth = FirebaseAuth.getInstance()

            val pendingResultTask = firebaseAuth.pendingAuthResult
            if (pendingResultTask != null) {
                pendingResultTask
                    .addOnSuccessListener { authResult ->
                        onTwitterLoginSuccess(authResult)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, ERROR_TWITTER + { e.message }, Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                firebaseAuth
                    .startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener { authResult ->
                        onTwitterLoginSuccess(authResult)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, LOGIN_ERROR + { e.message }, Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    }

    private fun onTwitterLoginSuccess(authResult: AuthResult) {
        val email = authResult.user?.email ?: NO_EMAIL
        val context = requireContext()

        saveEmail(context, email)
        setLoggedIn(context, true)


        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
