package com.example.cloudfirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cloudfirestore.data.DataRepository
import com.example.cloudfirestore.data.DataViewModel
import com.example.cloudfirestore.data.DataViewModelFactory
import com.example.cloudfirestore.data.model.User

class MainActivity : ComponentActivity() {

    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataRepository = DataRepository()
        val dataViewModelFactory = DataViewModelFactory(dataRepository)
        dataViewModel = ViewModelProvider(this, dataViewModelFactory).get(DataViewModel::class.java)

        setContent {
            val search = remember { mutableStateOf("us") }
            val users = remember { mutableStateOf(listOf<User>()) }

            dataViewModel.getUsers(search.value)
            lifecycleScope.launchWhenStarted {
                dataViewModel.responseUsers.collect{
                    users.value = it
                }
            }

            Scaffold(
                topBar = {
//                    TopAppBar(
//                        title = {
//                            TextField(
//                                value = search.value,
//                                onValueChange = { search.value = it },
//                                placeholder = {
//                                    Text(text = "Search")
//                                }
//                            )
//                        }
//                    )
                }, content = {
                    Column {
                        OutlinedButton(onClick = {
                            val user = User(
                                username = "username${(0..1000).random()}",
                                age = (0..1000).random()
                            )
                            dataViewModel.addUser(
                                user = user
                            )
                        }) {
                            Text(text = "Add user")
                        }

                        LazyColumn(content = {
                            items(users.value){item ->
                                Box(
                                    modifier = Modifier
                                        .pointerInput(Unit){
                                            detectTapGestures(
                                                onDoubleTap = {
                                                dataViewModel.deleteUser(item)
                                            }, onTap = {
                                                    item.age = (0..1000).random()
                                                    item.username = "update username ${(0..1000).random()}"
                                                    dataViewModel.updateUser(item)
                                                }
                                            )
                                        }
                                ) {
                                    Column {
                                        Text(
                                            text = item.username,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                        Text(
                                            text = item.age.toString(),
                                            modifier = Modifier.padding(5.dp)
                                        )
                                        OutlinedButton(
                                            modifier = Modifier.padding(10.dp),
                                            onClick = { dataViewModel.updateUsername(
                                            username = "username update ${(0..100).random()}",
                                            document = item.id
                                        ) }) {
                                            Text(text = "update username")
                                        }
                                        Divider()
                                    }
                                }
                            }
                        })
                    }
                }
            )
        }
    }
}