package bob.e2e

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import java.util.Base64
import kotlin.random.Random
import java.security.SecureRandom

data class Keypad(
    val keypadId: String,
    val shuffeledKeyHash: Array<String>,
    val keyHash: Array<String>,
    val imageBase64: String
)

class KeypadBuilder
{
    fun generateKeypad(): Keypad
    {
        // 이미지 파일 경로
        val imagePaths = listOf(
            "static/images/button0.png",
            "static/images/button1.png",
            "static/images/button2.png",
            "static/images/button3.png",
            "static/images/button4.png",
            "static/images/button5.png",
            "static/images/button6.png",
            "static/images/button7.png",
            "static/images/button8.png",
            "static/images/button9.png",
        )

        var keyHash = generateRandomKeyHash()
        var randomKeyHash = keyHash.copyOf()

        // 이미지 로드
        val images = imagePaths.map { path ->
            ImageIO.read(Thread.currentThread().contextClassLoader.getResourceAsStream(path))
        }

        // 배열 크기 설정
        val cols = 4
        val rows = 3

        // 비어 있는 슬롯을 포함한 배열 생성
        val grid = MutableList(rows * cols) { null }

        // 배열의 랜덤 위치 설정
        val positions = (0 until (rows * cols)).shuffled(Random(System.currentTimeMillis()))

        // 이미지와 랜덤 키 해시 배열의 위치 매핑
        val imagePositions = positions.subList(0, images.size)
        val keyHashPositions = positions

        // 이미지를 배열의 랜덤 위치에 배치
        imagePositions.forEachIndexed { index, pos ->
            grid[pos] = images[index]
        }

        // 랜덤 키 해시 배열을 동일한 위치로 재배열
        val shuffledKeyHash = Array(12) { "" }
        for (i in keyHashPositions.indices) {
            shuffledKeyHash[keyHashPositions[i]] = randomKeyHash[i % randomKeyHash.size]
        }

        // 각 이미지의 최대 크기 계산
        val maxWidth = images.maxOfOrNull { it.width } ?: 0
        val maxHeight = images.maxOfOrNull { it.height } ?: 0

        // 전체 이미지 크기 설정
        val totalWidth = cols * maxWidth
        val totalHeight = rows * maxHeight

        // 결합된 이미지 생성
        val combinedImage = BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = combinedImage.createGraphics()

        // 이미지 결합
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val index = i * cols + j
                val image = grid[index]
                if (image != null) {
                    graphics.drawImage(image, j * maxWidth, i * maxHeight, null)
                }
            }
        }
        graphics.dispose()

        // 결합된 이미지를 ByteArray로 변환
        val baos = ByteArrayOutputStream()
        ImageIO.write(combinedImage, "png", baos)
        val base64Image = Base64.getEncoder().encodeToString(baos.toByteArray())

        return Keypad(
            generateRandomString(64),
            shuffledKeyHash,
            keyHash,
            base64Image)
    }

    private fun generateRandomString(length: Int): String
    {
        val random = SecureRandom() // CSPRNG
        val byteArray = ByteArray(length)
        random.nextBytes(byteArray)

        return byteArray.joinToString("") {
            String.format("%02x", it)
        }
    }

    private fun generateRandomKeyHash(): Array<String>
    {
        var keyHashes = arrayOf(
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            generateRandomString(32),
            "null",
            "null"
        )

        return keyHashes
    }
}