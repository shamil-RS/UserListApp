package com.example.userlistapp.ui.theme.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.userlistapp.R
import com.example.userlistapp.domain.model.User
import com.example.userlistapp.ui.theme.UserListAppTheme
import com.example.userlistapp.ui.theme.screen.signup.SignUpViewModel
import com.example.userlistapp.util.LaIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateBack: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val userList by viewModel.viewModelState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                HomeChannel.Logout -> onNavigateToSignIn()
            }
        }
    }

    BackHandler(onBack = onNavigateBack)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 10.dp),
                title = {
                    if (user != null) {
                        UserProfile(
                            name = user!!.name,
                            img = (user!!.profilePicture ?: R.drawable.user).toString(),
                        )
                    } else {
                        Text("User not found")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                    }) {
                        Icon(
                            imageVector = LaIcons.Logout,
                            contentDescription = stringResource(id = R.string.logout)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            UserListAppTheme {
                LazyColumn {
                    item {
                        AllUsers(allUsers = userList.userList, currentUser = user) { user ->
                            viewModel.deleteUser(user)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllUsers(
    allUsers: List<User>,
    currentUser: User?,
    onDeleteUser: (User) -> Unit
) {
    Column {
        Text(text = "All users:")
        allUsers.forEach { user ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = user.profilePicture),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = user.name)
                    Text(text = user.birthDate!!)
                }
                if (currentUser != null && canDeleteUser (currentUser, user)) {
                    IconButton(onClick = { onDeleteUser(user) }) {
                        Icon(
                            imageVector = LaIcons.Remove,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun UserProfile(
    name: String,
    img: String,
) {

    val viewModel: SignUpViewModel = hiltViewModel()
    val profilePicture by viewModel.profilePicture.collectAsStateWithLifecycle()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = rememberAsyncImagePainter(profilePicture ?: img),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column {
            Text(text = "Hello!")
            Text(text = name, fontWeight = FontWeight.Bold)
        }
    }
}

private fun canDeleteUser (currentUser: User, user: User): Boolean {
    return currentUser.birthDate!! < user.birthDate!!
}