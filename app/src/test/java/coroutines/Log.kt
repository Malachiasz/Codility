package coroutines

import java.text.SimpleDateFormat
import java.util.Date

fun log(value: Any) {
    val dateFormat = SimpleDateFormat("HH:mm:ss")
    val millisInString = dateFormat.format(Date())
    System.out.println("[${Thread.currentThread().name} Current time: $millisInString] ${value}")
}
