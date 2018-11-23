import configuration.AppConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.ApplicationPidFileWriter

fun main(args: Array<String>) {
    val springApplication = SpringApplication(AppConfiguration::class.java)
    springApplication.addListeners(ApplicationPidFileWriter("app.pid"))
    springApplication.run(*args)
}




