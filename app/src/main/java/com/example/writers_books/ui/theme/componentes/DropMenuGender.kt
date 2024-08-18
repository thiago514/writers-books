package com.example.writers_books.ui.theme.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DropMenuGender(value: String, onValueChange: (string: String) -> Unit, modifier: Modifier = Modifier){
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {  },
            label = { Text("Genero") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Genero")
                }
            })
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Masculino") },
                onClick = { onValueChange("Masculino")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Feminino") },
                onClick = { onValueChange("Feminino")
                    expanded = false}
            )
            DropdownMenuItem(
                text = { Text("Outros") },
                onClick = { onValueChange("Outros")
                    expanded = false}
            )
        }
    }
}
