import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.musicapp.Data.Category

@Composable
fun MusicCategoriesScreen(
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit = {},
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        topBar()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category) {
                    onCategorySelected(category)
                }
            }
        }
    }
}

@Composable
fun TopBar(title: String) {
    TopAppBar(
        title = {
            Text(
                title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
private fun CategoryItem(category: Category, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val painter = rememberAsyncImagePainter(model = category.picture_medium)

            Image(
                painter = painter,
                contentDescription = category.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = category.name,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = Color.Black.copy(alpha = 0.7f))
                    .fillMaxWidth()
                    .padding(4.dp),
                style = MaterialTheme.typography.body2,
                color = Color.White, // Change the text color to white
                textAlign = TextAlign.Center
            )
        }
    }
}


