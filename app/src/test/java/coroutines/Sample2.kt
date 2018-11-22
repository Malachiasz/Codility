package coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Sample2 {

    private val threadPoolContext = newFixedThreadPoolContext(2, "ThreadPool");

    @Test
    fun test1() {
        runBlocking {
            log("start")
            launch(threadPoolContext) {
                val data = downloadDataAsync()
                // process the data on the UI thread
                log(data)
            }
            log("end")
        }
    }

    private suspend fun downloadDataAsync(): String {
        return suspendCoroutine { cont ->
            try {
                cont.resume(fetchResult2(0))
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
    }

    private fun fetchResult2(id: Int): String {
        Thread.sleep(1000)
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val millisInString = dateFormat.format(Date())
        if (id == 2) {
            throw NullPointerException("a nuller")
        }
        return "[${Thread.currentThread().name}:$millisInString]"
    }

    @Test
    fun test2() {
        runBlocking {
            log("start")
            var resultString = ""
            try {
                async(threadPoolContext) {
                    resultString = fetchResult2(1) + fetchResult2(3);
                    fetchResult2(2)
                }.await()
            } catch (e: Exception) {
                log(e.localizedMessage)
            }
            log("end, resultString:" + resultString)
        }
    }

}