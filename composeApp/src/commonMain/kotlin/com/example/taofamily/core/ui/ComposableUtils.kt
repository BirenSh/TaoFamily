package com.example.taofamily.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.taofamily.core.utils.AppConstant
import com.example.taofamily.features.initiation.domain.model.InternalLabelled
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopbar(
    title: String,
    containerColor:Color,
    onBack:(()-> Unit)? = null,
    onActionClick: (()-> Unit)? = null,
    trailingIcon: ImageVector?= null,

){
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor),

        //leading icon (back click)
        navigationIcon = {
            if (onBack != null){
                IconButton(
                    onClick = {onBack()},
                ){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back"
                    )

                }
            }
        },

        //trailing icon
        actions = {
            if (onActionClick != null && trailingIcon != null){
                IconButton(
                    onClick = {onActionClick()}
                ){
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = "action"
                    )
                }
            }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeHolder: String = "Search",
    modifier: Modifier
){
    TextField(
        value = query,
        onValueChange = {onQueryChange(it)},
        placeholder = { Text(text = placeHolder) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AppColors.PureWhite,
            unfocusedTextColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = AppColors.PureWhite,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,

        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .border(width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(18.dp)
            ),

    )
}

@Composable
fun FormInputText(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    modifier: Modifier,
    icon: ImageVector = Icons.Default.Edit,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxChar: Int = 100,
){
    TextField(
        value = value,
        onValueChange = {
            if (it.length <= maxChar) {
                onValueChange(it)
            }
        },
        placeholder = { Text(placeHolder) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "icon"
            )
        },
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AppColors.PureWhite,
            disabledContainerColor = AppColors.PureWhite,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            //hide under line transparent
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        visualTransformation = visualTransformation,
        maxLines = maxChar,

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T>FormDropdown(
    selectedValue: T,
    onValueSelected:(T)-> Unit,
    label: String,
    options: List<T>,
    icon: ImageVector,
    modifier: Modifier
)where T : Enum<T>, T:InternalLabelled {

    var expanded by remember { mutableStateOf(false) }

    // ExposedDropdownMenuBox is CRITICAL for the dropdown behavior
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
        modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp)

    ){
        TextField(
            value = selectedValue.label.takeIf { it.startsWith("Select") }?: selectedValue.label,
            onValueChange = {},
            placeholder = { Text(text = label)},
            leadingIcon = {Icon(imageVector = icon, contentDescription = "menu")},
            modifier = modifier.menuAnchor().fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(18.dp)
                ),
            shape = RoundedCornerShape(18.dp),
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppColors.PureWhite,
                disabledContainerColor = AppColors.PureWhite,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                //hide under line transparent
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),


        )

        //the drop down menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
        ){
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it.label) },
                    onClick = {
                        onValueSelected(it)
                        expanded = false // Close the menu
                    },
                    // Optional: Highlight selected item
                    leadingIcon = {
                        if (it == selectedValue) {
                            Icon(Icons.Filled.Check, contentDescription = "Selected")
                        }
                    },
                )
            }
        }
    }

}

@Composable
fun FormCheckBox(
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier
) {

    Row(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            .clickable(onClick = {onCheckChange(!checked)}),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckChange(it) },

            )
        Text(text = label)

    }

}


@Composable
fun ErrorDialog(
    isVisible: Boolean,
    errorMessage: String,
    onDismissCall:()-> Unit,
){
    // Only compose the Dialog if isVisible is true
    if (isVisible){
        Dialog(
            onDismissRequest = { onDismissCall },
        ){
            Card(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                colors = CardDefaults.cardColors(containerColor = AppColors.PureWhite),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "error",
                            tint = AppColors.StatusError,
                        )
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center

                        )
                    }
                    HorizontalDivider(Modifier.padding(vertical = 5.dp), DividerDefaults.Thickness, DividerDefaults.color)
                    TextButton(
                        onClick = onDismissCall,
                    ){
                        Text("Ok", color = AppColors.PrimaryBlack)
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun ErrorDialogPreview() {
    LoadingDialog(
        isVisible = true,
        labelText = "Submiting  form...",
        onDismissCall = {}
    )
}


@Composable
fun LoadingDialog(
    isVisible: Boolean,
    labelText: String,
    onDismissCall: () -> Unit
){
    if (isVisible){
        Dialog(
            onDismissRequest = { onDismissCall },
        ){
            Card(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                colors = CardDefaults.cardColors(containerColor = AppColors.PureWhite),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier. padding(20.dp),
                        color = AppColors.PrimaryOrange,

                    )

                    HorizontalDivider(Modifier.padding(vertical = 5.dp), DividerDefaults.Thickness, DividerDefaults.color)
                    Text(
                        text = labelText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center

                    )
                }
            }
        }
    }
}

@Composable
fun <T> SingleSelectFilterChips(
    options: List<T>,
    selected: T,
    onSelectionChange: (T) -> Unit
) where T : InternalLabelled, T : Any {

    options.forEach { option ->
        FilterChip(
            selected = option == selected,
            onClick = { onSelectionChange(option) },
            label = { Text(option.label) },
            // Use a simple primary color variant for selection
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = AppColors.PrimaryOrange.copy(alpha = 0.8f),
                selectedLabelColor = Color.Black
            ),
            trailingIcon = {
                if (option == selected){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected"
                    )
                }
            }
        )
    }
}

