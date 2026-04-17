package com.example.budgetbasket

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExpensePieChart(
    expensesByPerson: Map<String, Double>,
    totalExpenses: Double
) {
    val pieChartColors = listOf(
        Color(0xFF42A5F5),
        Color(0xFF66BB6A),
        Color(0xFFFFCA28),
        Color(0xFFEF5350),
        Color(0xFFAB47BC),
        Color(0xFF26C6DA)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        if (totalExpenses <= 0.0) return@Canvas

        var startAngle = -90f

        val chartSize = size.minDimension * 0.85f
        val topLeft = Offset(
            x = (size.width - chartSize) / 2f,
            y = (size.height - chartSize) / 2f
        )

        expensesByPerson.entries.forEachIndexed { index, entry ->
            val sweepAngle = ((entry.value / totalExpenses) * 360f).toFloat()

            drawArc(
                color = pieChartColors[index % pieChartColors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = topLeft,
                size = Size(chartSize, chartSize)
            )

            startAngle += sweepAngle
        }
    }
}