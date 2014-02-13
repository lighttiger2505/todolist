package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

import java.util.{Date}

case class Task(
    id       : Pk[Long],
    label    : String,
    date     : Option[Date],
    priority : Option[Int],
    memo     : Option[String]
)

object Task {

    val simple = {
        get[Pk[Long]]("id") ~
        get[String]("label") ~
        get[Option[Date]]("date") ~
        get[Option[Int]]("priority") ~
        get[Option[String]]("memo") map {
            case id~label~date~priority~memo => Task(id, label, date, priority, memo)
}
        }

    def all(): List[Task] = DB.withConnection { implicit c =>
        SQL("select * from task").as(Task.simple *)
    }

    def findById(id: Long): Option[Task] = {
        DB.withConnection { implicit c =>
            SQL("select * from task where id = {id}").on(
                'id -> id
            ).as(Task.simple.singleOpt)
            }
        }

    def create(task: Task) {
        DB.withConnection { implicit c =>
            SQL("""
                insert into 
                task (label, memo, date, priority) 
                values ({label}, {memo}, {date}, {priority})
                """).on(
                    'label    -> task.label,
                    'memo     -> task.memo,
                    'date     -> task.date,
                    'priority -> task.priority
                ).executeUpdate()
                }
        }

    def update(id: Long, task: Task) = {
        DB.withConnection { implicit c =>
            SQL("""
                update 
                task 
                set 
                label    = {label},
                date     = {date},
                priority = {priority}
                where id = {id}
                """).on(
                    'id       -> id,
                    'label    -> task.label,
                    'memo     -> task.memo,
                    'date     -> task.date,
                    'priority -> task.priority
                ).executeUpdate()
                }
        }

    def delete(id: Long) {
        DB.withConnection { implicit c =>
            SQL("delete from task where id = {id}").on(
                'id -> id
            ).executeUpdate()
            }
        }
    }
