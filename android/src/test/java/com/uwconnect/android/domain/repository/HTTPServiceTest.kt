import com.google.gson.JsonPrimitive
import com.uwconnect.android.data.LocalDateTimeAdapter
import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalDateTime
import org.junit.Test

class LocalDateTimeAdapterTest {
    private val localDateTimeAdapter = LocalDateTimeAdapter()

    @Test
    fun `test serialize localDateTime to JsonElement`() {
        val now = LocalDateTime(2021, 10, 10, 10, 10, 10)
        val jsonElement = localDateTimeAdapter.serialize(now, null, null)
        assertEquals(JsonPrimitive(now.toString()), jsonElement)
    }

    @Test
    fun `test deserialize JsonElement to localDateTime`() {
        val now = LocalDateTime(2021, 10, 10, 10, 10, 10)
        val jsonElement = JsonPrimitive(now.toString())
        val deserializedDateTime = localDateTimeAdapter.deserialize(jsonElement, null, null)
        assertEquals(now, deserializedDateTime)
    }
}