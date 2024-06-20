package com.uwconnect.android.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.presentation.ui.Black
import com.uwconnect.android.presentation.ui.DarkBlue
import com.uwconnect.android.presentation.ui.LightBlue


@Composable
fun AccountTypeToggle(
    modifier: Modifier = Modifier,
    onAccountTypeSelected: (AccountType) -> Unit
) {
    var selectedAccountType by remember { mutableStateOf(AccountType.Club) }

    Row(
        modifier = modifier.padding(top = 3.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                selectedAccountType = AccountType.Club
                onAccountTypeSelected(AccountType.Club)
            },
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedAccountType == AccountType.Club) Black else DarkBlue,
                contentColor = Color.White
            ),
            border = BorderStroke(2.dp, LightBlue),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Club")
        }
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = {
                selectedAccountType = AccountType.Member
                onAccountTypeSelected(AccountType.Member)
            },
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedAccountType == AccountType.Member) Black else DarkBlue,
                contentColor = Color.White
            ),
            border = BorderStroke(2.dp, LightBlue),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Member")
        }
    }
}