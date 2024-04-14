import com.example.v5rules.data.Chapter
import com.example.v5rules.data.Paragraph
import com.example.v5rules.data.Section
import com.google.gson.Gson
import java.io.File

class ChapterReader(private val jsonFilePath: String) {
    private val gson = Gson()

    fun readChapters(): List<Chapter> {
        val jsonContent = File(jsonFilePath).readText()
        val chapters = gson.fromJson(jsonContent, Array<ChapterData>::class.java).map { chapterData ->
            chapterData.toChapter()
        }
        return chapters
    }

    private data class ChapterData(
        val title: String,
        val content: String?,
        val sections: List<SectionData>
    ) {
        fun toChapter(): Chapter {
            val sections = sections.map { sectionData ->
                Section(
                    title = sectionData.title,
                    content = sectionData.content.orEmpty(),
                    keywords = sectionData.keywords.orEmpty(),
                    paragraphs = sectionData.paragraphs.map { paragraphData ->
                        Paragraph(
                            title = paragraphData.title,
                            content = paragraphData.content.orEmpty(),
                            keywords = paragraphData.keywords.orEmpty()
                        )
                    }
                )
            }
            return Chapter(title = title, content = content.orEmpty(), sections = sections)
        }
    }

    private data class SectionData(
        val title: String,
        val content: String?,
        val keywords: List<String>?,
        val paragraphs: List<ParagraphData>
    )

    private data class ParagraphData(
        val title: String,
        val content: String?,
        val keywords: List<String>?
    )
}