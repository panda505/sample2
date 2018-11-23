package repository.impl

import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import repository.GroupRepository

data class Group(val id: Int, val name: String)

@Repository
class GroupRepositoryImpl(val jdbcTemplate: NamedParameterJdbcTemplate) : GroupRepository {
    @Transactional
    override fun selectAll(): List<Group> {
        val parameters = mapOf<String, Any>("name1" to "n1", "name2" to "n2", "name3" to "n3")
//        jdbcTemplate.update("INSERT INTO groups(gr_name) values(:name1)", parameters)
//        jdbcTemplate.update("INSERT INTO groups(gr_name) values(:nme2)", parameters)
//        jdbcTemplate.update("INSERT INTO groups(gr_name) values(:name3)", parameters)
        return jdbcTemplate.queryForList("SELECT gr_id, gr_name FROM groups", parameters).map {
            Group(it["gr_id"] as Int, it["gr_name"] as String)
        }
    }

    override fun selectByID(id: Int): Group? = try {
        val parameters = mapOf<String, Any>("id" to id)
        val sql = "SELECT gr_id, gr_name FROM groups WHERE gr_id = :id"
        val group = jdbcTemplate.queryForMap(sql, parameters)
        Group(group["gr_id"] as Int, group["gr_name"] as String)
    } catch (e: DataAccessException) {
        null
    }

    override fun deleteById(id: Int) {
        val parameters = mapOf<String, Any>("id" to id)
        jdbcTemplate.update("DELETE from groups WHERE gr_id = :id", parameters)
    }

    override fun addNewGroup(name: String) {
        val parameters = mapOf<String, Any>("name" to name)
        jdbcTemplate.update("INSERT INTO groups(gr_name) values(:name)", parameters)

    }

    override fun studentsOfTheGroup(grId: Int): List<Student?> {
        val parameters = mapOf<String, Any>("grId" to grId)
        val sql = "SELECT st_id, st_name, gr_id FROM students INNER JOIN groups USING(gr_id) WHERE gr_id =  :grId"
        return jdbcTemplate.queryForList(sql, parameters).map {
            Student(it["st_id"] as Int, it["st_name"] as String, it["gr_id"] as Int)
        }
    }
}

