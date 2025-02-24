package com.karua.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karua.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Surface( modifier = Modifier.fillMaxSize())  {
                    TipCalculatorLayout()
                }
            }
        }
    }
}

@VisibleForTesting
internal fun calculateTip(amount : Double,
                         tipPercent : Double,
                         roundUp : Boolean) : String{
    var tip = tipPercent / 100 * amount

    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)

}

@Composable
fun TipCalculatorLayout(modifier: Modifier = Modifier) {

    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember {mutableStateOf("")}
    var roundUp by remember { mutableStateOf(false) }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, roundUp)

    Surface {
        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.calculate_tip),
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
                    .align(alignment = Alignment.Start)
            )
            EditNumberField(
                label = R.string.bill_amount,
                leadingIcon = R.drawable.money,
                value = amountInput,
                onValueChange = {amountInput = it},
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )
            EditNumberField(
                label = R.string.how_was_the_service,
                leadingIcon = R.drawable.percent,
                value = tipInput,
                onValueChange = {tipInput = it},
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )
            RoundTheTipRow(
                roundUp = roundUp,
                onRoundUpChanged = {roundUp = it},
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Text(
                text = stringResource(R.string.tip_amount, tip),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
fun EditNumberField(
    modifier: Modifier = Modifier,
    label: Int,
    @DrawableRes leadingIcon : Int,
    value: String,
    onValueChange: (String) -> Unit = {},
    keyBoardOptions : KeyboardOptions,

){
    OutlinedTextField(
        label = { Text(stringResource(label)) },
        leadingIcon = { Icon(painter = painterResource(leadingIcon), null) },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(topStart = 5.dp, bottomEnd = 5.dp),
        keyboardOptions = keyBoardOptions
    )
}

@Composable
fun RoundTheTipRow(
    roundUp : Boolean,
    onRoundUpChanged : (Boolean) -> Unit,
    modifier: Modifier = Modifier){
    Row(modifier = Modifier
        .fillMaxWidth()
        .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun TimeLayoutPreview() {
    TipCalculatorTheme {
        TipCalculatorLayout()
    }
}