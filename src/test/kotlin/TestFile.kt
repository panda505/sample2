import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import configuration.AppConfiguration
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import services.SuperMath
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.junit.Before
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import repository.impl.Student


@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [AppConfiguration::class])
@WebAppConfiguration(value = "")
@TestExecutionListeners(DependencyInjectionTestExecutionListener::class,
                        DbUnitTestExecutionListener::class)
class StudentTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var mapper: ObjectMapper

    lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Ignore
    @Test
    fun givenGreetURI_whenMockMVC_thenVerifyResponse() {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()

        Assert.assertEquals("text/plain;charset=UTF-8", mvcResult.response.contentType)
        Assert.assertEquals("Hello World!!!", mvcResult.response.contentAsString)
    }

    @Test
    fun getStudentByIdTest() {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/students/24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("St12"))
                .andReturn()
    Assert.assertEquals("""{"id":24,"name":"St12","gr_id":14}""", mvcResult.response.contentAsString)
    }

    @Test
    fun postTest() {
        val student = Student(1,"fromTest", 4)
        val json = mapper.writeValueAsString(student)
        val mvcResult= mockMvc.perform(post("/students/post/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
        val s = mapper.readValue(mvcResult.response.contentAsString, Student::class.java)
        println(s.id)
//        println(mvcResult.response.contentAsString)
        Assert.assertEquals("""{"id":4,"name":"fromTest","gr_id":4}""", mvcResult.response.contentAsString)
    }

    @Ignore
    @Test
    fun givenWac_whenServletContext_thenItProvidesGreetController() {
        val servletContext = webApplicationContext.getServletContext()
        Assert.assertNotNull(servletContext)
        Assert.assertTrue(servletContext is MockServletContext)
        Assert.assertNotNull(webApplicationContext.getBean("greetController"))
    }

    @Ignore
    @Test
    fun selectByIdest(){
        val math = SuperMath()
        Assert.assertEquals(3, math.onePlus(3))
    }

    @Test
    @DatabaseSetup( "sampleData.xml" )
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "expectedData.xml")
    fun testInsert() {
        mockMvc.perform(get("/students/db"))
    }

}
