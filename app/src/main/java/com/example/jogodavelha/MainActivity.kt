package com.example.jogodavelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import com.example.jogodavelha.ui.theme.BLACK
import com.example.jogodavelha.ui.theme.WHITE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppJogoDaVelha()
        }
    }
}
@Composable
fun AppJogoDaVelha() {
    var telaAtual by remember { mutableStateOf(Tela.MENU) }

    var imagemJogadorX by remember { mutableStateOf(0) }
    var imagemJogadorO by remember { mutableStateOf(0) }
    var vencedorFinal by remember { mutableStateOf<String?>(null) }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (telaAtual) {
            Tela.MENU -> {
                TelaMenu(
                    onJogoComecar = { idJogadorX, idJogadorO ->
                        imagemJogadorX = idJogadorX
                        imagemJogadorO = idJogadorO
                        telaAtual = Tela.JOGO
                    }
                )
            }

            Tela.JOGO -> {
                JogoDaVelha(
                    imagemJogadorX = imagemJogadorX,
                    imagemJogadorO = imagemJogadorO,
                    onJogoTerminou = { vencedor ->
                        vencedorFinal = vencedor
                        telaAtual = Tela.TELA_VENCEDOR
                    }
                )
            }

            Tela.TELA_VENCEDOR -> {
                val imagemVencedor = when (vencedorFinal) {
                    "X" -> imagemJogadorX
                    "O" -> imagemJogadorO
                    else -> 0
                }

                TelaVencedor(
                    vencedor = vencedorFinal,
                    imagemVencedor = imagemVencedor,
                    onVoltarAoMenu = {
                        telaAtual = Tela.MENU
                    }
                )
            }
        }
    }
}

/**
 *Menu
 */
@Composable
fun TelaMenu(onJogoComecar: (jogadorX: Int, jogadorO: Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WHITE)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Escolha os Jogadores",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = BLACK
        )
        Spacer(modifier = Modifier.height(60.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val personagens = listOf("Superman" to R.drawable.supermano, "Batman" to R.drawable.batmano)
            personagens.forEach { (nome, imagemId) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onJogoComecar(R.drawable.supermano, R.drawable.batmano) }
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = imagemId),
                        contentDescription = "Personagem $nome",
                        modifier = Modifier.size(120.dp)
                    )
                    Text(text = nome, style = MaterialTheme.typography.bodyLarge, color = BLACK)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Clique em um personagem para começar!",
            style = MaterialTheme.typography.bodyMedium,
            color = BLACK.copy(alpha = 0.7f)
        )
    }
}

/**
 * A tela do Jogo da Velha. Sua única responsabilidade é gerenciar o jogo
 * e informar quando ele termina.
 */
@Composable
fun JogoDaVelha(
    imagemJogadorX: Int,
    imagemJogadorO: Int,
    onJogoTerminou: (vencedor: String?) -> Unit
) {
    var bloco by remember { mutableStateOf(List(9) { "" }) }
    var jogadorAtual by remember { mutableStateOf("X") }

    // Esta parte verifica o vencedor a cada jogada.
    val vencedor = verificaVencedor(bloco)
    if (vencedor != null) {
        LaunchedEffect(Unit) {
            onJogoTerminou(vencedor)
        }
    }

    fun cliqueDoBloco(index: Int) {
        if (bloco[index].isEmpty() && vencedor == null) {
            bloco = bloco.toMutableList().apply { this[index] = jogadorAtual }
            jogadorAtual = if (jogadorAtual == "O") "X" else "O"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WHITE)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Turno:",
                style = MaterialTheme.typography.headlineLarge,
                color = BLACK,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(id = if (jogadorAtual == "X") imagemJogadorX else imagemJogadorO),
                contentDescription = "Turno do jogador atual",
                modifier = Modifier.size(50.dp)
            )
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Grade(
                bloco = bloco,
                imagemJogadorX = imagemJogadorX,
                imagemJogadorO = imagemJogadorO,
                cliqueBloco = ::cliqueDoBloco
            )
        }
    }
}

/**
 * tela vencedor ou o empate.
 */
@Composable
fun TelaVencedor(
    vencedor: String?,
    imagemVencedor: Int,
    onVoltarAoMenu: () -> Unit
) {
    val mensagem: String
    val imagemFinal: Int?

    if (vencedor == "Empate") {
        mensagem = "Deu Empate!"
        imagemFinal = null
    } else {
        mensagem = "O Vencedor é..."
        imagemFinal = imagemVencedor
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WHITE)
            .padding(16.dp)
            .clickable { onVoltarAoMenu() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = mensagem,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = BLACK
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (imagemFinal != null && imagemFinal != 0) {
            Image(
                painter = painterResource(id = imagemFinal),
                contentDescription = "Vencedor",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Toque na tela para voltar ao menu",
            style = MaterialTheme.typography.bodyLarge,
            color = BLACK.copy(alpha = 0.7f)
        )
    }
}


@Composable
fun Grade(
    bloco: List<String>,
    imagemJogadorX: Int,
    imagemJogadorO: Int,
    cliqueBloco: (Int) -> Unit
) {
    Column {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    CriaQuadradoUI(
                        value = bloco[index],
                        imagemJogadorX = imagemJogadorX,
                        imagemJogadorO = imagemJogadorO,
                        onClick = { cliqueBloco(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun CriaQuadradoUI(
    value: String,
    imagemJogadorX: Int,
    imagemJogadorO: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(WHITE)
            .border(5.dp, BLACK)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = value.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                    scaleIn(animationSpec = tween(durationMillis = 300))
        ) {

            when (value) {
                "X" -> {
                    Image(
                        painter = painterResource(id = imagemJogadorX),
                        contentDescription = "Jogador X",
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    )
                }
                "O" -> {
                    Image(
                        painter = painterResource(id = imagemJogadorO),
                        contentDescription = "Jogador O",
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    )
                }
            }
        } }
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