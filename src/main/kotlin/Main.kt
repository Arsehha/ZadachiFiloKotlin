import kotlin.random.Random

data class Philosopher(val id: Int, var hasFork: Boolean = false)

fun main() {
    println("Введите количество философов:")
    val philosophersCount = readLine()!!.toInt()

    val philosophers = List(philosophersCount) { Philosopher(it + 1) }

    val forks = MutableList(philosophersCount) { false }

    fun pickFork(philosopher: Philosopher): Boolean {
        val leftFork = (philosopher.id - 1 + philosophersCount) % philosophersCount
        val rightFork = philosopher.id % philosophersCount

        val randomChoice = Random.nextBoolean()

        if (randomChoice) {
            if (!forks[rightFork]) {
                forks[rightFork] = true
                philosopher.hasFork = true
                println("Философ ${philosopher.id} взял вилку справа")
                return true
            }
        }
        else {
            if (!forks[leftFork]) {
                forks[leftFork] = true
                philosopher.hasFork = true
                println("Философ ${philosopher.id} взял вилку слева")
                return true
            }
        }

        return false
    }

    val randomStartIndex = Random.nextInt(philosophersCount)
    val philosophersLeft = philosophers.toMutableList()

    val firstPhilosopher = philosophersLeft[randomStartIndex]
    philosophersLeft.remove(firstPhilosopher)

    println("Философ ${firstPhilosopher.id} начинает первым.")
    val successFirst = pickFork(firstPhilosopher)

    if (!successFirst) {
        println("Философ ${firstPhilosopher.id} не смог взять вилку, но будет пытаться в следующем раунде")
        philosophersLeft.add(firstPhilosopher)
    }

    var round = 1
    var currentIndex = (randomStartIndex + 1) % philosophersCount

    while (philosophersLeft.isNotEmpty()) {
        println("\nРаунд $round:")

        val philosophersAttempting = philosophersLeft.toList()
        philosophersLeft.clear()

        for (i in philosophersAttempting.indices) {
            val philosopher = philosophersAttempting[i]
            if (!philosopher.hasFork) {
                val success = pickFork(philosopher)
                if (!success) {
                    val leftFork = (philosopher.id - 1 + philosophersCount) % philosophersCount
                    val rightFork = philosopher.id % philosophersCount

                    // Если обе вилки заняты, философ не будет пытаться в этом раунде
                    if (forks[leftFork] && forks[rightFork]) {
                        println("Философ ${philosopher.id} не может взять вилку, и обе вилки заняты, он будет пытаться в следующем раунде")
                    } else {
                        // Если хотя бы одна вилка свободна, философ будет пытаться в следующем раунде
                        philosophersLeft.add(philosopher)
                    }
                }
            }
        }
        round++
    }

    // Вывод результата
    println("\nРезультат:")
    philosophers.forEach { philosopher ->
        if (philosopher.hasFork) {
            println("Философ ${philosopher.id} обедает")
        } else {
            println("Философ ${philosopher.id} размышляет")
        }
    }
}
