package web

import org.springframework.web.bind.annotation.*
import repository.impl.Group
import repository.impl.Student
import repository.GroupRepository
import repository.StudentRepository
import services.GitHubService
import services.User
import javax.servlet.http.HttpServletResponse


@RestController
class greetController {
    @GetMapping("/")
    fun helloWorld() = "Hello World!!!"
}

@RestController
@RequestMapping("/students")
class StudentController(val studentRepository: StudentRepository,
                        val groupRepository: GroupRepository,
                        val gitHubService: GitHubService) {

    private fun studentExist(id: Int, response: HttpServletResponse) {
        val student = studentRepository.selectByID(id)
        if (student == null) response.status = 400
    }

    @GetMapping("/db")
    fun addPerson() = studentRepository.addPerson()

    @GetMapping("/gs")
    fun retrofit(): User? = gitHubService.getUser("octocat").execute().body()


    @PostMapping("/post")
    fun postAddStudent(@RequestBody student: Student, response: HttpServletResponse): Student? {
        val group = groupRepository.selectByID(student.grId)
        if (group == null) response.status = 400
        return studentRepository.addNewStudent(student.name, student.grId)
    }

    @GetMapping("/")
    fun all(): List<Student?> = studentRepository.selectAll()


    @GetMapping("/{id}")
    fun byId(@PathVariable id: Int, response: HttpServletResponse): Student? {
        studentExist(id, response)
        return studentRepository.selectByID(id)
    }

    @GetMapping("/delete/{id}")
    fun deleteById(@PathVariable id: Int, response: HttpServletResponse) {
        studentExist(id, response)
        return studentRepository.deleteById(id)
    }

    @GetMapping("/add/name={name}&grId={grId}")
    fun addNewStudent(@PathVariable name: String,
                      @PathVariable grId: Int,
                      response: HttpServletResponse): Student? {
        val group = groupRepository.selectByID(grId)
        if (group == null) response.status = 400
        return  studentRepository.addNewStudent(name, grId)
    }

    @GetMapping("/update/id={id}&newName={newName}")
    fun updateStudent(@PathVariable id: Int,
                      @PathVariable newName: String,
                      response: HttpServletResponse) {
        studentExist(id, response)
        return studentRepository.updateStudent(id, newName)
    }
}

@RestController
@RequestMapping("/groups")
class GroupController(val groupRepository: GroupRepository) {


    private fun groupExist(id: Int, response: HttpServletResponse) {
        val student = groupRepository.selectByID(id)
        if (student == null) response.status = 400
    }

    @GetMapping("/")
    fun all(): List<Group?> = groupRepository.selectAll()

    @GetMapping("/{id}")
    fun byId(@PathVariable id: Int, response: HttpServletResponse) {
        groupExist(id, response)
        groupRepository.selectByID(id)
    }

    @GetMapping("/delete/{id}")
    fun deleteById(@PathVariable id: Int, response: HttpServletResponse) {
        groupExist(id, response)
        groupRepository.deleteById(id)
    }

    @GetMapping("/add/{name}")
    fun addNewGroup(@PathVariable name: String) = groupRepository.addNewGroup(name)

    @GetMapping("/studentsOF/{id}")
    fun studentsOfTheGroup(@PathVariable id: Int) = groupRepository.studentsOfTheGroup(id)
}

