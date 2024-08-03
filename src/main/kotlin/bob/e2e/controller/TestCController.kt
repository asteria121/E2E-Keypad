package bob.e2e.controller

import bob.e2e.KeypadBuilder
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class EnduserKeypad(
    val keypadId: String,
    val shuffeledKeyHash: Array<String>,
    val imageBase64: String
)

@RestController
class TestCController {
    @GetMapping("/data", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getData(): EnduserKeypad {
        var kb = KeypadBuilder()
        var pad = kb.generateKeypad()

        var i = 0
        for (item in pad.keyHash) {
            println(i.toString() + ": " + item)
            i++
        }

        var endKeypad = EnduserKeypad(
            pad.keypadId,
            pad.shuffeledKeyHash,
            pad.imageBase64
        )

        return endKeypad
    }

    @GetMapping("/test")
    fun getTest(): String {
        return "test"
    }
}