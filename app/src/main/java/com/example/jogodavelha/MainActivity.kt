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
import androidx.compose.ui.text.font.FontWeight
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
    var vencedor by remember {
        mutableStateOf<String?>(null)
    }

    fun cliqueDoBloco(index: Int) {
        if (bloco[index] == "" && vencedor == null) {
            bloco = bloco.toMutableList().apply {
                this[index] = jogadorAtual
            }
            jogadorAtual = if (jogadorAtual == "0") "X" else "0"
            vencedor = verificaVencedor(bloco)
        }
    }

    // O Box permite alinhar elementos em diferentes partes da tela (topo, centro, etc.)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WHITE)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter) // Alinha este Row no topo do Box
                .padding(vertical = 40.dp), // Aumenta o espaçamento do topo
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val statusTexto = when (vencedor) {
                "Empate" -> "Deu Empate!"
                null -> "Jogador Atual: $jogadorAtual"
                else -> "Vencedor: $vencedor"
            }

            Text(
                text = statusTexto,
                style = MaterialTheme.typography.headlineLarge,
                color = BLACK,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Reiniciar Jogo",
                tint = BLACK, // Cor do ícone
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        bloco = List(9) { "" }
                        jogadorAtual = "X"
                        vencedor = null
                    }
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center), // Alinha este Column no centro do Box
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Grade(bloco = bloco, cliqueBloco = ::cliqueDoBloco)
        }
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
        Text(
            text = value, style = MaterialTheme.typography.headlineMedium,
            color = BLACK,
            fontWeight = FontWeight.ExtraBold,
        )

    }
}

private fun verificaVencedor(bloco: List<String>): String? {
    val combinacoesVencedoras = listOf(
        // Linhas
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        // Colunas
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        // Diagonais
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    for (combinacao in combinacoesVencedoras) {
        val (a, b, c) = combinacao
        if (bloco[a].isNotEmpty() && bloco[a] == bloco[b] && bloco[a] == bloco[c]) {
            return bloco[a]
        }
    }

    return if (bloco.all { it.isNotEmpty() }) "Empate" else null
}



