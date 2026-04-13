package com.example.budgetbasket

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val pieChartColors = listOf(
    Color(0xFF42A5F5),
    Color(0xFF66BB6A),
    Color(0xFFFFCA28),
    Color(0xFFEF5350),
    Color(0xFFAB47BC),
    Color(0xFF26C6DA)
)

@Composable
fun ExpensePieChart(expensesByPerson: Map<String, Double>) {
    val total = expensesByPerson.values.sum()

    if (total <= 0.0) {
        Text(
            text = "No expense data available",
            color = Color.Black
        )
        return
    }

    Canvas(
        modifier = Modifier.size(220.dp)
    ) {
        var startAngle = -90f

        expensesByPerson.entries.forEachIndexed { index, entry ->
            val sweepAngle = ((entry.value / total) * 360f).toFloat()

            drawArc(
                color = pieChartColors[index % pieChartColors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(size.width, size.height)
            )

            startAngle += sweepAngle
        }
    }
}