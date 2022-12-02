import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.File
import java.io.FileNotFoundException
import java.lang.RuntimeException
import java.nio.charset.Charset

fun main(args: Array<String>) {

    val subscriptions = CompositeDisposable()

    val observableSatu = Observable.create<String> {
        it.onNext("Satu")
        it.onError(RuntimeException("Error"))
        it.onNext("Dua")
        it.onComplete()
    }

    val observableDua = Observable.create<String> {
        it.onNext("Tiga")
    }

    val subscriberSatu = observableSatu.subscribeBy(
        onNext = { println(it)},
        onComplete = { println("Selesai")},
        onError = { println(it)}
    )
    subscriptions.add(subscriberSatu)

    val subscriberDua = observableDua.subscribeBy(
        onNext = { println(it) }
    )
    subscriptions.add(subscriberDua)

    fun loadText(filename: String): Single<String> {
        return Single.create create@{ emitter ->
            val file = File(filename)
            if (!file.exists()) {
                emitter.onError(FileNotFoundException("File tidak ditemukan $filename"))
                return@create
            }
            val contents = file.readText(Charsets.UTF_8)
            emitter.onSuccess(contents)
        }
    }

    val subscriberText = loadText("baca.txt").subscribeBy(
        onSuccess = { println(it)},
        onError = { println("Error, $it")}
    )
    subscriptions.add(subscriberText)

    subscriptions.dispose()

}