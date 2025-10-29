package collector.worker

import collector.engine.CollectEngine
import collector.engine.port.DeduplicatePort
import collector.engine.port.Publisher
import collector.worker.config.TargetProperties
import collector.worker.provider.AdapterProvider
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class CollectorRegistry(
    private val props: TargetProperties,
    private val collectorFactory: CollectorFactory,
    ) {

    private val collectors = mutableListOf<Collector>()

    @PostConstruct
    fun registerWorkers() {
        //worker 등록
        props.entries.forEach { (target, config) ->

            //worker 생성
            collectors.add(collectorFactory.createCollector(target, config))
            //TODO: worker 생성 실패 시 에러 핸들링
        }
    }

    fun executeAll() {
        collectors.forEach {
            it.collectWork()
        }

        //TODO: worker 실행 실패 시 에러 핸들링
        //TODO: coroutine 을 적용한 병렬 실행
        //TODO: coroutine 을 적용한 병렬 실행
    }
}