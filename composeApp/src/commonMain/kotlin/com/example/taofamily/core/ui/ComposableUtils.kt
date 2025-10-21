package com.example.taofamily.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
){
    TextField(
        value = value,
        onValueChange = {onValueChange(it)},
        placeholder = {Text(placeHolder)},
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
enum class SampleEnum(override val label: String) : InternalLabelled {
    OPTION_A("Option A"),
    OPTION_B("Option B"),
    OPTION_C("Option C")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FormDropdownPreview() {
    // Define a dummy enum that implements InternalLabelled for the preview

    val options = SampleEnum.entries
    var selectedOption by remember { mutableStateOf(options[0]) }

    FormDropdown(
        selectedValue = selectedOption,
        onValueSelected = { selectedOption = it },
        label = "Sample Dropdown",
        options = options,
        icon = Icons.Default.ArrowCircleDown,
        modifier = Modifier.padding(16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppSearchBarPreview() {
    AppSearchBar(
        query = "Sample Query",
        onQueryChange = {},
        placeHolder = "Search...",
        modifier = Modifier
    )
}