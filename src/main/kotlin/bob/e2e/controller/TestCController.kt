package bob.e2e.controller

import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.util.Base64
import kotlin.random.Random

data class ResponseData(
    val randomValue: Int,
    val imageBase64: String
)

@RestController
class TestCController {
    @GetMapping("/data", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getData(@RequestParam("key") key: Int): ResponseData {
        // 랜덤 값 생성
        val randomValue = Random.nextInt(100) // 예: 0과 99 사이의 랜덤 값

        // 이미지 파일을 Base64로 인코딩
        val imageBase64 = encodeImageToBase64(key)

        return ResponseData(
            randomValue = randomValue,
            imageBase64 = imageBase64
        )
    }

    @GetMapping("/test")
    fun getTest(): String {
        return "test"
    }

    private fun encodeImageToBase64(key: Int): String {
        try
        {
            val imagePath = "static/images/button$key.png"
            val resource = ClassPathResource(imagePath)

            val imageBytes = resource.inputStream.readBytes()
            return Base64.getEncoder().encodeToString(imageBytes)
        }
        catch (ex: Exception)
        {
            println(ex.message)
            return "ERROR"
        }

    }
}