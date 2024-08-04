package com.compose.presentation.views.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
@Composable
fun ErrorMsgCard(errorMsg:String){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.pink)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = (1.dp),
            color = colorResource(R.color.light_red)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    )
    {
        Text(
            text = errorMsg,
            color = colorResource(R.color.red),
            modifier = Modifier.padding(20.dp)
        )
    }
}