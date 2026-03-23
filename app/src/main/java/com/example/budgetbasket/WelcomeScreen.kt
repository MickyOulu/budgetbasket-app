package com.example.budgetbasket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgetbasket.ui.theme.BudgetBasketTheme


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onGetStartedClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83D\uDED2",
            style = MaterialTheme.typography.headlineLarge,
        )

        Text(
            text = "BudgetBasket",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Plan grocery shopping and track shared expenses easily.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
        )

        Button(onClick = onGetStartedClick) {
            Text(text = "Get Started")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    BudgetBasketTheme {
        WelcomeScreen(onGetStartedClick = {})
    }
}