package repository.impl

import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import repository.StudentRepository
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource



data class Student(val id: Int , val name: String, val grId: Int)

@Repository
class StudentRepositoryImpl(val jdbcTemplate: NamedParameterJdbcTemplate) : StudentRepository {

    override fun addPerson(){
        val parameters = mapOf<String, Any>()
        jdbcTemplate.update("INSERT INTO person( title, firstName, lastName) values( 'Mr', 'Phil', 'Webber')", parameters)
    }

    override fun selectAll(): List<Student?>{
        val parameters = mapOf<String, Any>()
        return jdbcTemplate.queryForList("SELECT * FROM students", parameters).map {
            Student(it["st_id"] as Int, it["st_name"] as String, it["gr_id"] as Int)
        }
    }

    override fun selectByID(id: Int): Student? = try {
        val parameters = mapOf<String, Any>("id" to id)
        val sql = "SELECT st_id, st_name, gr_id FROM students WHERE st_id = :id"
        val student = jdbcTemplate.queryForMap(sql, parameters)
        Student(student["st_id"] as Int, student["st_name"] as String, student["gr_id"] as Int)
    }  catch (e: DataAccessException){
        null
    }

    override fun deleteById(id: Int){
        val parameters = mapOf<String, Any>("id" to id)
        jdbcTemplate.update("DELETE from students WHERE st_id = :id", parameters)
    }

    override fun addNewStudent(name: String, grId: Int): Student?{
        val parameters = MapSqlParameterSource()
                .addValue("name", name)
                .addValue("grId", grId)
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update("INSERT INTO students(st_name, gr_id) values(:name, :grId)", parameters, keyHolder)
        val id =  keyHolder.getKeys()?.get("st_id") as Int
        return selectByID(id)
    }

    override fun updateStudent(id: Int, newName: String){
        val parameters = mapOf<String, Any>("id" to id, "new_name" to newName)
        jdbcTemplate.update("UPDATE students SET st_name = :new_name WHERE st_id = :id", parameters)
    }
}

