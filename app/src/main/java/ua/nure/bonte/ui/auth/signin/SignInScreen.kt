package ua.nure.bonte.ui.auth.signin

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.nure.bonte.App
import ua.nure.bonte.R
import ua.nure.bonte.config.WEB_CLIENT_ID
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteInputField
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme

private val TAG by lazy { "SignInScreen" }

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                SignIn.Event.OnBack -> navController.navigateUp()
                is SignIn.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    SignInScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun SignInScreenContent(
    state: SignIn.State,
    onAction: (SignIn.Action) -> Unit
) {
    BonteScreen {
        val context = LocalContext.current
        val credentialManager = remember { CredentialManager.create(context = context) }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.padding(top = 70.dp),
                    painter = painterResource(if (isSystemInDarkTheme()) R.drawable.bonte_dark else R.drawable.bonte),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.signInMessage),
                    style = AppTheme.typography.regular
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BonteInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .padding(top = AppTheme.dimension.normal),
                    label = stringResource(R.string.email),
                    value = state.email
                ) {
                    onAction(SignIn.Action.OnEmailChange(email = it))
                }
                BonteInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .padding(top = AppTheme.dimension.normal),
                    label = stringResource(R.string.password),
                    value = state.password,
                    isPassword = true
                ) {
                    onAction(SignIn.Action.OnPasswordChange(password = it))
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .padding(top = AppTheme.dimension.normal)
                        .clickable {
                            onAction(SignIn.Action.OnNavigate(route = Screen.Auth.ForgotPassword))
                        },
                    text = stringResource(R.string.forgotPassword),
                    style = AppTheme.typography.regular.copy(
                        color = AppTheme.color.active,
                        textAlign = TextAlign.End
                    ),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BonteButton(
                    modifier = Modifier.padding(top = 40.dp),
                    textModifier = Modifier.padding(horizontal = AppTheme.dimension.normal),
                    text = stringResource(R.string.login)
                ) {
                    onAction(SignIn.Action.OnSignIn)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .padding(top = AppTheme.dimension.normal),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .weight(1F)
                            .background(AppTheme.color.grey)
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimension.normal),
                        text = stringResource(R.string.continueWith),
                        style = AppTheme.typography.small.copy(
                            color = AppTheme.color.grey
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .weight(1F)
                            .background(AppTheme.color.grey)
                    )
                }
            }

            BonteButton(
                modifier = Modifier.padding(top = 40.dp),
                icon = R.drawable.google,
                text = stringResource(R.string.google)
            ) {

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(WEB_CLIENT_ID)
                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        val result: GetCredentialResponse = credentialManager.getCredential(
                            request = request,
                            context = context
                        )

                        val credential = result.credential

                        when (credential) {
                            // Passkey credential
                            is PublicKeyCredential -> {
                                // Share responseJson such as a GetCredentialResponse on your server to
                                // validate and authenticate
                                val responseJson = credential.authenticationResponseJson
                            }
                            // Password credential
                            is PasswordCredential -> {
                                // Send ID and password to your server to validate and authenticate.
                                val username = credential.id
                                val password = credential.password
                            }
                            // GoogleIdToken credential
                            is CustomCredential -> {
                                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                    try {
                                        // Use googleIdTokenCredential and extract the ID to validate and
                                        // authenticate on your server.
                                        val googleIdTokenCredential = GoogleIdTokenCredential
                                            .createFrom(credential.data)
                                        // You can use the members of googleIdTokenCredential directly for UX
                                        // purposes, but don't use them to store or control access to user
                                        // data. For that you first need to validate the token:
                                        // pass googleIdTokenCredential.getIdToken() to the backend server.
//                                        GoogleIdTokenVerifier verifier = ... // see validation instructions
//                                        GoogleIdToken idToken = verifier.verify(idTokenString);
                                        // To get a stable account identifier (e.g. for storing user data),
                                        // use the subject ID:
//                                        idToken.getPayload().getSubject()

                                        Log.d(
                                            TAG,
                                            "SignInScreenContent: Google idToken: ${googleIdTokenCredential.idToken}"
                                        )
                                        Log.d(
                                            TAG,
                                            "SignInScreenContent: Google credential: ${googleIdTokenCredential.id}"
                                        )

                                        onAction(
                                            SignIn.Action.OnGoogleSignIn(
                                                idToken = googleIdTokenCredential.idToken,
                                                email = googleIdTokenCredential.id
                                            )
                                        )

                                    } catch (ex: GoogleIdTokenParsingException) {
                                        Log.e(
                                            TAG,
                                            "SignInScreenContent: Received an invalid google id token response",
                                            ex
                                        )
                                    }
                                } else {
                                    Log.e(TAG, "Unexpected type of credential")
                                }
                            }

                            else -> {
                                Log.e(TAG, "Unexpected type of credential")
                            }

                        }

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(bottom = 40.dp)
                    .clickable {
                        onAction(SignIn.Action.OnNavigate(Screen.Auth.Registration))
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(end = AppTheme.dimension.small),
                    text = stringResource(R.string.dontHaveAnAcc),
                    style = AppTheme.typography.regular
                )
                Text(
                    text = stringResource(R.string.signUp),
                    style = AppTheme.typography.regular.copy(
                        color = AppTheme.color.active
                    ),
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun SingInScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            SignInScreenContent(
                state = SignIn.State()
            ) {}
        }
    }

}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SingInScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            SignInScreenContent(
                state = SignIn.State()
            ) {}
        }
    }

}