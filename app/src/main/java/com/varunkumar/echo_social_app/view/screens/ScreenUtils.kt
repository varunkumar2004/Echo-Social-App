package com.varunkumar.echo_social_app.view.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionBar(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    header: String,
    trailingIcon: ImageVector? = null,
    onClickLeadingIcon: () -> Unit = {},
    onClickTrailingIcon: () -> Unit = {}
) {
    val modify = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)

    Box(
        modifier = modify
    ) {
        if (leadingIcon != null) {
            IconButton(
                modifier = Modifier.align(
                    Alignment.CenterStart
                ),
                onClick = { onClickLeadingIcon() }) {
                Icon(imageVector = leadingIcon, contentDescription = null)
            }
        }

        Text(
            text = header,
            modifier = modify.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        if (trailingIcon != null) {
            IconButton(
                modifier = Modifier.align(
                    Alignment.CenterEnd
                ),
                onClick = { onClickTrailingIcon() }) {
                Icon(imageVector = trailingIcon, contentDescription = null)
            }
        }
    }
}

@Composable
fun RoundTextField(
    modifier: Modifier = Modifier,
    header: String?,
    cornerSize: Dp = 40.dp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClickTrailingIcon: () -> Unit = {},
    trailingIcon: ImageVector? = null,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    var query by remember {
        mutableStateOf("")
    }

    val radius = RoundedCornerShape(cornerSize)

    Column(
        modifier = modifier
    ) {
        if (header != null) {
            Text(text = header)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(TextFieldDefaults.MinHeight)
                .clip(radius)
                .border(1.dp, Color.DarkGray, radius),
        ) {
            BasicTextField(
                modifier = modifier
                    .weight(
                        if (trailingIcon != null) 0.8f else 1f
                    )
                    .padding(horizontal = 20.dp),
                visualTransformation = visualTransformation,
                singleLine = singleLine,
                value = query,
                onValueChange = {
                    query = it
                    onValueChange(query)
                }
            )

            if (trailingIcon != null) {
                IconButton(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = {
                        onClickTrailingIcon()
                    }) {
                    Icon(imageVector = trailingIcon, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun RegisterPage(modifier: Modifier) {
    var email by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )
    {
        RoundTextField(
            modifier = modifier,
            header = "email",
            singleLine = true,
            onValueChange = {
                email = it
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UtilsPreview() {
    RegisterPage(Modifier.fillMaxWidth())
}