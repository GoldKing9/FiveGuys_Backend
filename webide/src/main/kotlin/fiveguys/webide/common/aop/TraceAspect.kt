package fiveguys.webide.common.aop

import fiveguys.webide.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class TraceAspect {
    val log = logger()

    @Around("@annotation(fiveguys.webide.common.aop.Trace)")
    fun doTrace(joinPoint: ProceedingJoinPoint): Any? {
        val traceId = generateOrRetrieveTraceId()
        return try {
            MDC.put("traceId",traceId)

            val startTime = System.currentTimeMillis()
            val result = joinPoint.proceed()
            val endTime = System.currentTimeMillis()
            log.info("REQUEST TRACING_ID: {} / Time: {}ms / Method: {} / Args: {}", traceId, endTime - startTime, joinPoint.signature.toShortString(), joinPoint.args);
            result
        }finally {
            MDC.remove("traceId")
        }

    }

    private fun generateOrRetrieveTraceId(): String {
        var traceId = ThreadLocalContextHolder.getTraceId()
        if (traceId == null) {
            traceId = UUID.randomUUID().toString()
            ThreadLocalContextHolder.setTraceId(traceId)
        }
        return traceId
    }

}
