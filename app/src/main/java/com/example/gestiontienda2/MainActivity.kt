package com.example.gestiontienda2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.example.gestiontienda2.presentation.ui.auth.LoginScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gestiontienda2.presentation.ui.auth.LoginScreen
//import com.example.gestiontienda2.presentation.ui.reports.Top10ProductsByProfitMarginScreen
//import com.example.gestiontienda2.presentation.ui.reports.Top10ProductsSoldScreen
//import com.example.gestiontienda2.presentation.ui.reports.CompanyProposalScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            YourAppTheme {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen() }
                    //composable("top10profit") { Top10ProductsByProfitMarginScreen() }
                    //composable("top10sold") { Top10ProductsSoldScreen() }
                    //composable("companyproposal") { CompanyProposalScreen() }
                    // TODO: Add registration route
                }
            }
        }
    }
}
