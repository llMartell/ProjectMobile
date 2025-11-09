package com.example.jogodavelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jogodavelha.ui.theme.BLACK
import com.example.jogodavelha.ui.theme.DARKGREEN
import com.example.jogodavelha.ui.theme.JogoDaVelhaTheme
import com.example.jogodavelha.ui.theme.WHITE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JogoDaVelha()
        }
    }
}

@Composable
fun JogoDaVelha() {
    var bloco by remember {
        mutableStateOf(List(9) { "" })
    }
    var jogadorAtual by remember {
        mutableStateOf("X")
    }
    val vencedor by remember {
        mutableStateOf<String?>(null)
    }

    fun cliqueDoBloco(index: Int) {
        if (bloco[index] == "" && vencedor == null) {
            bloco = bloco.toMutableList().apply {
                this[index] = jogadorAtual
            }
            jogadorAtual = if (jogadorAtual == "X") "0" else "X"
        }

    }
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jogador:X",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = null,
                tint = BLACK,
                modifier = Modifier.size(30.dp)
            )
        }
        Grade(bloco = bloco, cliqueBloco = ::cliqueDoBloco)
    }

}

@Composable
fun Grade(
    bloco: List<String>, cliqueBloco: (Int) -> Unit
) {
    Column {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    CriaQuadradoUI(value = bloco[index], onClick = { cliqueBloco(index) })
                }
            }
        }
    }
}


@Composable
fun CriaQuadradoUI(
    value: String, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(WHITE)
            .border(5.dp, BLACK)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

    }
}


