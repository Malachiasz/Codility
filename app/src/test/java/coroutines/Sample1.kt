package coroutines

import com.google.common.util.concurrent.ThreadFactoryBuilder
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Sample1 {

    @Test
    fun test1_playingWithAsync() {
        runBlocking {
            val deffered: MutableList<Deferred<String>> = ArrayList()
            for (i in 1..10) {
                deffered.add(async { fetchResult(id = i) })
            }
            val results = awaitAll(*deffered.toTypedArray())
            log(results)
        }
    }

    private suspend fun fetchResult(id: Int): String {
        return suspendCoroutine { cont ->
            try {
                sleep(1000)
                val dateFormat = SimpleDateFormat("mm:ss")
                val millisInString = dateFormat.format(Date())
                if (id == 2) {
                    throw NullPointerException("a nuller")
                }
                cont.resume("[RESULT:$millisInString]")
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
    }

    @Test
    fun test2_runningStuffInParallerAndWaitingForResultCoroutines() {
        val threadPoolContext = newFixedThreadPoolContext(2, "ThreadPool");

        runBlocking {
            try {
                doSomethingWithResult(
                        async(threadPoolContext) { fetchResult(1) },
                        async(threadPoolContext) { fetchResult(4) },
                        async(threadPoolContext) { fetchResult(3) }
                )
            } catch (e: Exception) {
                System.out.println("Exception got caught: ${e.message}")
            }
        }
    }

    private suspend fun doSomethingWithResult(async: Deferred<String>, async1: Deferred<String>, async2: Deferred<String>) {
        System.out.println(async.await() + async1.await() + async2.await())
    }

    @Test
    fun test2_runningStuffInParallerAndWaitingForResultExecutor() {
        val executor = Executors.newFixedThreadPool(2)
        try {
            doSomethingWithResult2(
                    executor.submit { fetchResult2(1) },
                    executor.submit { fetchResult2(2) },
                    executor.submit { fetchResult2(3) }
            )
        } catch (e: Exception) {
            System.out.println("Exception got caught: ${e.message}")
        }

    }

    private fun fetchResult2(id: Int): String {
        sleep(1000)
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val millisInString = dateFormat.format(Date())
        if (id == 2) {
            throw NullPointerException("a nuller")
        }
        return "[RESULT:$millisInString]"
    }

    private fun doSomethingWithResult2(submit: Future<*>, submit1: Future<*>, submit2: Future<*>) {
        System.out.println("" + submit.get() + submit1.get() + submit2.get())
    }

    @Test
    fun test4() = runBlocking {
        val job = GlobalScope.launch {
            println("Throwing exception from launch")
            throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
        }
        job.join()
        println("Joined failed job")
        val deferred = GlobalScope.async {
            println("Throwing exception from async")
            throw ArithmeticException() // Nothing is printed, relying on user to call await
        }
        try {
            deferred.await()
            println("Unreached")
        } catch (e: ArithmeticException) {
            println("Caught ArithmeticException")
        }
    }

    lateinit var uiConfinedString: String;

    // Caller thread is not blocked
    @Test
    fun test5_runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines() {
        val backgroundContext = newFixedThreadPoolContext(2, "Background");
        val uiContext = newFixedThreadPoolContext(1, "UIThread");



        runBlocking {
            launch{
                delay(500)
                print("hola")
            }
            try {
                async(backgroundContext) {
                    log("Computing result")
                    val result = fetchResult(1)
                    async(uiContext) {
                        uiConfinedString = result
                        log("Publishing result $result")
                        throw java.lang.NullPointerException("aa")
                    }
                }
            } catch (e: Exception) {
                System.out.println("Exception got caught: ${e.message}")
            }
            log("END")
        }
    }


    // submition blocks caller thread
    @Test
    fun test5_runOnBackgroundThreadAndPublishesOnCurrentThreadThreadPool() {
        // Guave used for ThreadFactoryBuilder
        val backgroundContext = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("Background").build())
        val uiContext = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("UIThread").build())

        try {
            backgroundContext.submit{
                sleep(500)
                print("hola")
            }

            backgroundContext.submit {
                log("Computing result")
                val result = fetchResult2(1)
                uiContext.submit {
                    uiConfinedString = result
                    log("Publishing result $result")
                    throw java.lang.NullPointerException("aa")
                }.get()
            }.get()
        } catch (e: Exception) {
            System.out.println("Exception got caught: ${e.message}")
        }
        log("END")
    }

    // Version 2
    @Test
    fun test5_runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines_NonBlocking() {
        val backgroundContext = newFixedThreadPoolContext(2, "Background");
        val uiContext = newFixedThreadPoolContext(1, "UIThread");

        runBlocking {
            runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines(uiContext, backgroundContext)
        }
    }


    private suspend fun runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines(uiThread: CoroutineContext, backgroundThread: CoroutineContext) {
        log("START suspend function")
        try {
            withContext(backgroundThread) {
                log("Computing result")
                val result = fetchResult(1)
                withContext(uiThread) {
                    uiConfinedString = result
                    log("Publishing result $result")
                    throw java.lang.NullPointerException("aa")
                }
            }
        } catch (e: Exception) {
            System.out.println("Exception got caught: ${e.message}")
        }
        log("END suspend function")
    }

    // Version 2b
    @Test
    fun test5_runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines_NonBlocking2b() {
        val backgroundContext = newFixedThreadPoolContext(2, "Background");
        val uiContext = newFixedThreadPoolContext(1, "UIThread");

        runBlocking {
            try {
                withContext(backgroundContext) {
                    log("Computing result")
                    val result = fetchResult(1)
                    withContext(uiContext) {
                        uiConfinedString = result
                        log("Publishing result $result")
                        throw java.lang.NullPointerException("aa")
                    }
                }
            } catch (e: Exception) {
                System.out.println("Exception got caught: ${e.message}")
            }
            log("END suspend function")
        }
    }


    // Version 3
    @Test
    fun test5_runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines_NonBlocking2() {
        val backgroundContext = newFixedThreadPoolContext(2, "Background");
        val uiContext = newFixedThreadPoolContext(1, "UIThread");

        runBlocking(uiContext) {
            runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines2(backgroundContext)
        }
    }

    private suspend fun runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines2(backgroundThread: CoroutineContext) {
        log("START suspend function")
        try {
            var result: String = ""
            withContext(backgroundThread) {
                log("Computing result")
                result = fetchResult(1)
            }
            uiConfinedString = result
            log("Publishing result $result")
            throw java.lang.NullPointerException("aa")
        } catch (e: Exception) {
            System.out.println("Exception got caught: ${e.message}")
        }
        log("END suspend function")
    }

    // Version 4
    @Test
    fun test5_runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines_NonBlocking3() {
        val backgroundContext = newFixedThreadPoolContext(2, "Background");
        val uiContext = newFixedThreadPoolContext(1, "UIThread");

        runBlocking(uiContext) {
            runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines3(backgroundContext)
        }
    }

    private suspend fun runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines3(backgroundThread: CoroutineContext) {
        log("START suspend function")
        try {
            log("Computing result")
            val result: String = fetchResult(1, backgroundThread)
            uiConfinedString = result
            log("Publishing result $result")
            throw java.lang.NullPointerException("aa")
        } catch (e: Exception) {
            System.out.println("Exception got caught: ${e.message}")
        }
        log("END suspend function")
    }



    private suspend fun fetchResult(id: Int, backgroundThread: CoroutineContext): String {
        log("START suspend function fetchResult()")
        return withContext(backgroundThread) {
            sleep(1000)
            val dateFormat = SimpleDateFormat("mm:ss")
            val millisInString = dateFormat.format(Date())
            if (id == 2) {
                throw NullPointerException("a nuller")
            }
            log("END suspend function fetchResult()")
            return@withContext "[RESULT:$millisInString]"
        }
    }

    // Version 4b
    @Test
    fun test5_runOnBackgroundThreadAndPublishesOnCurrentThreadCoroutines_NonBlocking4() {
        val backgroundContext = newFixedThreadPoolContext(2, "Background");
        val uiContext = newFixedThreadPoolContext(1, "UIThread");

        runBlocking(uiContext) {
            log("START suspend function")
            try {
                log("Computing result")
                val result: String = fetchResult2(1, backgroundContext)
                uiConfinedString = result
                log("Publishing result $result")
                throw java.lang.NullPointerException("aa")
            } catch (e: Exception) {
                System.out.println("Exception got caught: ${e.message}")
            }
            log("END suspend function")
        }
    }

    private suspend fun fetchResult2(id: Int, backgroundThread: CoroutineContext): String {
        log("START suspend function fetchResult()")
        return withContext(backgroundThread) {
            sleep(1000)
            val dateFormat = SimpleDateFormat("mm:ss")
            val millisInString = dateFormat.format(Date())
            if (id == 2) {
                throw NullPointerException("a potential exception")
            }
            log("END suspend function fetchResult()")
            return@withContext "[RESULT:$millisInString]"
        }
    }
}


