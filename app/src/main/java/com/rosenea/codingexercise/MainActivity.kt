package com.rosenea.codingexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rosenea.codingexercise.ui.theme.CodingExerciseTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URL

/**
 * Return the URL which contains the data for this coding exercise.
 */
internal val SOURCE_URL get() = URL("https://fetch-hiring.s3.amazonaws.com/hiring.json")

/**
 * Data class allowing deserializing the JSON acquired from [SOURCE_URL].
 */
@Serializable
data class Data(val id: Int, val listId: Int, val name: String?)

/**
 * Responsible for requesting all [Data] from [SOURCE_URL]. Once received, the data is forwarded to
 * the given [callback].
 */
internal fun requestData(callback: (List<Data>) -> Unit) {
    Thread {
        callback(Json.decodeFromString(SOURCE_URL.source))
    }.start()
}

/**
 * Responsible for returning testing data for the composable previews.
 */
private val TEST_DATA get() = mutableListOf(
    Data(755, 2, ""),
    Data(203, 2, ""),
    Data(684, 1, "Item 684"),
    Data(276, 1, "Item 276"),
    Data(736, 3, null),
    Data(926, 4, null),
    Data(808, 4, "Item 808"),
    Data(599, 1, null),
    Data(424, 2, null),
    Data(444, 1, ""),
    Data(809, 3, null),
    Data(293, 2, null),
    Data(510, 2, null),
    Data(680, 3, "Item 680"),
    Data(231, 2, null),
    Data(534, 4, "Item 534")
)

/**
 * Retrieve the data from this URL as a String.
 */
internal fun URL.read(): String {
    val result = StringBuilder()
    openStream().bufferedReader().use { reader ->
        var c: Int
        while (reader.read().also { c = it } != -1)
            result.append(c.toChar())
    }
    return result.toString()
}

/**
 * Retrieve the URL source content as a string.
 */
internal val URL.source: String get() {
    val result = StringBuilder()
    openStream().bufferedReader().use { reader ->
        var c: Int
        while (reader.read().also { c = it } != -1)
            result.append(c.toChar())
    }
    return result.toString()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loadingData = remember { mutableStateOf(true) }
            val data = remember { mutableListOf<Data>() }

            CodingExerciseTheme {
                Box(
                    modifier = Modifier
                        // Provide spacing to avoid conflict with the cutout.
                        .padding(top = 20.dp)
                        .fillMaxSize()
                ) {
                    DataContainer(loadingData.value, data)
                }
            }

            // Retrieve the data from the server.
            requestData { allData ->
                // Process the data
                allData
                    // Filter out all entries where name is null or empty.
                    .filter { !it.name.isNullOrEmpty() }
                    // When displaying, sort the elements first by listId, then by name.
                    .sortedWith { item0, item1 ->
                        var result = item0.listId.compareTo(item1.listId)

                        if (result == 0)
                            result = item0.name!!.compareTo(item1.name!!)

                        result
                    }
                    // Add all the elements to data
                    .run { data.addAll(this) }

                // Clear the lazy loading indicator.
                loadingData.value = false
            }
        }
    }
}

@Composable
fun DataContainer(
    loadingData: Boolean = true,
    data: MutableList<Data> = mutableListOf()
) {
    if (loadingData) {
        DataPlaceholder()
    } else {
        DataList(data)
    }
}

@Composable
fun DataCard(item: Data) {
    OutlinedCard(
        modifier = Modifier.padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = "${item.listId}  ${item.name}",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun DataList(allData: MutableList<Data>) {
    LazyColumn {
        items(allData) {
            DataCard(it)
        }
    }
}

@Composable
fun DataPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Retrieving and Processing Data...")
        Spacer(modifier = Modifier.height(5.dp))
        CircularProgressIndicator()
    }
}

@Preview(widthDp = 200)
@Composable
fun DataCardPreview() {
    CodingExerciseTheme {
        DataCard(
            TEST_DATA[2]
        )
    }
}

@Preview
@Composable
fun DataPlaceholderPreview() {
    CodingExerciseTheme {
        DataPlaceholder()
    }
}

@Preview
@Composable
fun DataListPreview() {
    CodingExerciseTheme {
        DataList(TEST_DATA)
    }
}

@Preview
@Composable
fun DataContainerPreview() {
    CodingExerciseTheme {
        DataContainer(
            true
        )
    }
}