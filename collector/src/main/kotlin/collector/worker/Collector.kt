package collector.worker

import collector.engine.CollectEngine
import collector.engine.command.CollectCommand

class Collector(
    val name: String,
    val collectionTask: CollectionTask,
    val engine: CollectEngine,
) {

    fun collectWork() {

        val command = CollectCommand(collectionTask.url)

        engine.run(command)
    }
}