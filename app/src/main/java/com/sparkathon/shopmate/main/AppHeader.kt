import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sparkathon.shopmate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(isInStoreMode: Boolean, onModeSwitch: () -> Unit, isHomePage: Boolean, onCartClick: () -> Unit) {
    // Header layout with primary background color
    TopAppBar(
        title = { Text(text = "ShopMate", color = MaterialTheme.colorScheme.onSecondary) },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                if (isHomePage) {
                    Button(
                        onClick = onModeSwitch,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = if (isInStoreMode) "Online" else "In-Store")
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onCartClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = "Cart",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}
