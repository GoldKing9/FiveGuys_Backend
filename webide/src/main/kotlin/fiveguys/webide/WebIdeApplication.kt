package fiveguys.webide

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class WebIdeApplication

fun main(args: Array<String>) {
    runApplication<WebIdeApplication>(*args)
}
