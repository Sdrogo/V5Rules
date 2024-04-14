import com.example.v5rules.data.Chapter
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val chapters: List<Chapter>
)